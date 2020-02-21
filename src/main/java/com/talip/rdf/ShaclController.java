package com.talip.rdf;

import org.apache.jena.riot.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/convert")
public class ShaclController {
    private static final Logger log = LoggerFactory.getLogger(ShaclController.class);

    @Autowired
    private ShaclLoader shaclLoader;

    private String hello = "Hello %s!";

    // mettre l'annotation controller
    // inject ShaclLoader
    // 3 endpoint => 1. retourne le model en rdfxml, 1 en jsonld, 1 en turtle
    // donc 3 getMapping


    @GetMapping("/request")
    public Message requestParameter(@RequestParam("name") String name) {
        // http://localhost:8080/convert/request?name=Talip
        String message = String.format(hello, name);
        log.info(message);
        return new Message(message);
    }

    @GetMapping("/path/{name}")
    public Message pathParameter(@PathVariable("name") String name) {
        //http://localhost:8080/convert/path/Talip
        String message = String.format(hello, name);
        log.info(message);
        return new Message(message);
    }

    @GetMapping(value = "/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String rdfXmlConverter() {
        //http://localhost:8080/convert/xml
        return shaclLoader.convert(Lang.RDFXML.getLabel());
    }

    @GetMapping(value = "/json-ld", produces = MediaType.APPLICATION_JSON_VALUE)
    public String jsonLdConverter() {
        return shaclLoader.convert(Lang.JSONLD.getLabel());

    }

    @GetMapping(value = "/ttl", produces = "text/turtle")
    public String ttlConverter() {
        return shaclLoader.convert(Lang.TURTLE.getLabel());

    }

    @GetMapping(value = "/query-model", produces = MediaType.APPLICATION_XML_VALUE)
    public String requestParameter(@RequestParam(value = "subject", required = false) String subject,
                                   @RequestParam(value = "predicate", required = false) String predicate) {

        // "http://www.dumbdata.orgoio/bilateralShape"
        //   ResourceFactory.createProperty("http://www.w3.org/ns/shacl#name"
        return shaclLoader.queryModel(subject, predicate, Lang.RDFXML.getLabel());
    }
}
