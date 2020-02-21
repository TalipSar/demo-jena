package com.talip.rdf;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
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

    }

    public Model getModel() {
        return model;
    }

    public String convert(String lang) {
        StringWriter writer = new StringWriter();
        model.write(writer, lang);
        return writer.toString();
    }
}

