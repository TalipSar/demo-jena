package com.talip.rdf;

import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.riot.Lang;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.StringWriter;

@Component
public class ShaclLoader implements CommandLineRunner {

    @Value("classpath:rdf/shacl.ttl")
    private Resource shacl;

    private Model model = ModelFactory.createDefaultModel();

    private static final Logger log = LoggerFactory.getLogger(ShaclLoader.class);

    @Override
    public void run(String... args) throws Exception {
        //log.info(IOUtils.toString(shacl.getInputStream(), Charset.defaultCharset()));

//        StringWriter writer = new StringWriter();
//
//        model.write(writer,Lang.RDFXML.getLabel());
//
//        log.info(writer.toString());

        model.read(shacl.getInputStream(), null, Lang.TURTLE.getLabel());
        this.sparqlQuery("http://www.dumbdata.orgoio/bilateralShape");

    }

    public Model getModel() {
        return model;
    }

    public String convert(String lang) {
        return convert(model, lang);
    }

    public String convert(Model modelToConvert, String lang) {
        StringWriter writer = new StringWriter();
        modelToConvert.write(writer, lang);
        return writer.toString();
    }

    public String queryModel(String subjectUri, String predicateUri, String lang) {
        Model modelFiltered = model.listStatements(
                Strings.isNotEmpty(subjectUri) ? ResourceFactory.createProperty(subjectUri) : null,
                Strings.isNotEmpty(predicateUri) ? ResourceFactory.createProperty(predicateUri) : null,
                (RDFNode) null)
                .toModel();
        return convert(modelFiltered, lang);
    }

    public void sparqlQuery(String subjectUri) {
        String query = String.format("SELECT ?s ?p ?o where {<%s> ?p ?o}", subjectUri);
        try (RDFConnection conn = RDFConnectionFactory.connect(DatasetFactory.create().setDefaultModel(model))) {
            conn.querySelect(query, (qs) -> {
                qs.varNames().forEachRemaining(p -> log.info("key: {}, value:{}", p, qs.get(p).toString()));
            });
        }
    }

}

