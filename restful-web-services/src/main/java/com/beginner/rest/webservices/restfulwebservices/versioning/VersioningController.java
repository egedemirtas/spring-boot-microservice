package com.beginner.rest.webservices.restfulwebservices.versioning;

import com.beginner.rest.webservices.restfulwebservices.versioning.data.PersonV1;
import com.beginner.rest.webservices.restfulwebservices.versioning.data.PersonV2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("versioning")
public class VersioningController {

    /**
     * Versioning with URI
     */
    @GetMapping("/v1/person")
    public PersonV1 getPersonV1() {
        return new PersonV1("Ege Demirtas");
    }

    @GetMapping("/v2/person")
    public PersonV2 getPersonV2() {
        return new PersonV2("Ege", "Demirtas");
    }

    //-----------------------------------------------------------

    /**
     * Versioning with request param
     */
    @GetMapping(path = "/person/reqParam", params = "version=1")
    public PersonV1 getPersonReqParamV1() {
        return new PersonV1("Ege Demirtas");
    }

    @GetMapping(path = "/person/reqParam", params = "version=2")
    public PersonV2 getPersonReqParamV2() {
        return new PersonV2("Ege", "Demirtas");
    }
    //-----------------------------------------------------------

    /**
     * Versioning with header
     */
    @GetMapping(path = "/person/header", headers = "X-API-VERSION=1")
    public PersonV1 getPersonReqHeaderV1() {
        return new PersonV1("Ege Demirtas");
    }

    @GetMapping(path = "/person/header", headers = "X-API-VERSION=2")
    public PersonV2 getPersonReqHeaderV2() {
        return new PersonV2("Ege", "Demirtas");
    }

    //-----------------------------------------------------------

    /**
     * Versioning with accept header (aka content negotiation)
     */
    @GetMapping(path = "/person/acceptHeader", produces = "application/vnd.company.app-v1+json")
    public PersonV1 getPersonAcceptHeaderV1() {
        return new PersonV1("Ege Demirtas");
    }

    @GetMapping(path = "/person/acceptHeader", produces = "application/vnd.company.app-v2+json")
    public PersonV2 getPersonAcceptHeaderV2() {
        return new PersonV2("Ege", "Demirtas");
    }
}
