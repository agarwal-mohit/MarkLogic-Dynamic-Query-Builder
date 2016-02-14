xquery version "1.0-ml";

declare variable $actual-input := xs:string(xdmp:get-request-field("query", ""));

let $tokenized-input1 := fn:tokenize($actual-input,",")

let $tokenized-input2 := for $i in $tokenized-input1
                            let $temp := fn:tokenize($i,":")
                            let $values := fn:tokenize($temp[2],"@")
                            let $tokenizedValues := for $value in $values
                                                        let $operator := fn:tokenize($value,"\^")
                                                        return <a><value>{$operator[1]}</value><operator>{$operator[2]}</operator></a>
                            return <root><param>{$temp[1]}</param><values>{$tokenizedValues}</values></root>

let $tokenized-input3 := <par>{$tokenized-input2}</par>

let $uri := cts:uri-match("/*",(),())

let $queries := for $i in $tokenized-input3//root
                    let $qname := fn:QName("", $i/param)
                    return
                    if (fn:count($i//value) eq 1) then
                        let $operator := $i//operator/text()
                        return
                            if ($operator eq "gt") then
                                cts:element-range-query($qname, ">", fn:number($i//value))
                            else if ($operator eq "lt") then
                                cts:element-range-query($qname, "<", fn:number($i//value))
                            else
                                cts:element-value-query($qname,$i//value)
                    else
                        let $queries:= for $ii in $i//a
                        let $value := $ii/value
                        let $operator := $ii/operator
                        return
                            if ($operator eq "gt") then
                                cts:element-range-query($qname, ">", fn:number($value))
                            else if ($operator eq "lt") then
                                cts:element-range-query($qname, "<", fn:number($value))
                            else
                                cts:element-value-query($qname,$value)

let $or-queries := cts:or-query($queries)

return $or-queries

let $final-query := cts:and-query($queries)

let $results := cts:search(doc($uri),$final-query)

return <Docs>{$results}</Docs>