package controllers

import config.Dependencies._

object SearchControllerInstance extends SearchController(marklogicDocumentsClient,ResutsJsonTransformer)


