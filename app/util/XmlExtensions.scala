package util

import scala.xml.{Elem, Node, NodeSeq, TopScope}

object XmlExtensions {

  implicit class NodeSeqOps(nodeSeq: NodeSeq) {
    def findRequired(predicate: Node => Boolean): Node = {
      nodeSeq.find(predicate).getOrElse(throw new NoSuchElementException("Could not find required node"))
    }

    def getOnlyNode: Node = {
      nodeSeq.size match {
        case 0 => throw new NoSuchElementException
        case 1 => nodeSeq.head
        case _ => throw new IllegalStateException("too many nodes")
      }
    }

    def getOptionalNode : Option[Node] = {
      nodeSeq.size match {
        case 0 => None
        case 1 => Some(nodeSeq.head)
        case _ => throw new IllegalStateException("too many nodes")
      }
    }

    def getNodeByAttribute(attributeKey: String, attributeValue: String): Option[Node] = {
      nodeSeq.find(node => {
        node.attribute(attributeKey) match {
          case Some(x) => x.head.text == attributeValue
          case None => false
        }
      })
    }

    def getNodesByAttribute(attributeKey: String, attributeValue: String): Seq[Node] = {
      nodeSeq.filter(node => {
        node.attribute(attributeKey) match {
          case Some(x) => x.head.text == attributeValue
          case None => false
        }
      })
    }

    def getOptionalValueFor(child: String) : String = {
      nodeSeq match {
        case NodeSeq.Empty => ""
        case `nodeSeq` if nodeSeq.head.label == child => nodeSeq.text
        case _ => (nodeSeq \\ child).getOptionalValueFor(child)
      }
    }

    def getOrElseText(text: String) : String = {
      nodeSeq match {
        case NodeSeq.Empty => text
        case ns if ns.text.equals("") =>  text
        case _ => nodeSeq.text
      }
    }
  }

  implicit class NodeOps(val node: Node) extends AnyVal {

    def attributeText(attribute: String): Option[String] = node.attribute(attribute).map(_.text)

    def removeNamespace(): Elem = {
      def transform(node: Node): Node = {
        node match {
          case e: Elem => e.copy(scope = TopScope, child = e.child map (transform))
          case _ => node
        }
      }
      node.asInstanceOf[Elem].copy(scope = TopScope, child = node.child map (transform))
    }

    def nodeTextWithChild: String = node.child.map(_.toString).mkString.trim

    def hasAttribute(attributeName: String): Boolean = node.attributes.exists(attr => attr.key == attributeName)

    def rawContents: String = {
      // using toString instead of text gets the raw contains instead of the encoded contains.
      node.toString.replaceAll(s"<${node.label}[^>]*>", "").replaceAll(s"</${node.label}>", "")
    }
  }
}