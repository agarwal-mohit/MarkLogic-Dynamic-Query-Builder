xquery version "1.0-ml";

import module namespace search = "http://marklogic.com/appservices/search" at "/MarkLogic/appservices/search/search.xqy";
declare variable $keyword := xs:string(xdmp:get-request-field("query", ""));


let $options := <options xmlns="http://marklogic.com/appservices/search">
                    <search-option>unfiltered</search-option>
                    <return-query>false</return-query>
                    <return-results>true</return-results>
                    <return-facets>false</return-facets>
                    <transform-results apply="empty-snippet"/>
                    <term>
                    <term-option>punctuation-insensitive</term-option>
                    <term-option>case-insensitive</term-option>
                    </term>
                    </options>

let $results := search:resolve-nodes(search:parse($keyword),$options)

let $a := for $i in $results
            return $i//*[text()=$keyword]

return
<contexts>{
for $i in $a/name()
return <name>{$i}</name>
}
</contexts>
