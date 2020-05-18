package com.mate.velkey.links.controller;

import com.mate.velkey.links.model.link.Link;
import com.mate.velkey.links.model.link.LinkRequest;
import com.mate.velkey.links.service.LinkService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "links")
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping
    public ResponseEntity<Iterable<Link>> getLinks() {
        return ResponseEntity.ok()
                .body(linkService.getLinks());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Link> getLinkById(@PathVariable String id) {
        return ResponseEntity.ok()
                .body(linkService.getLinkResponse(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Link> createLink(@RequestPart LinkRequest linkRequest,
                                           @RequestPart MultipartFile image) throws IOException {
        return ResponseEntity.ok()
                .body(linkService.createLink(linkRequest, image));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Link> updateLink(@PathVariable String id,
                                           @RequestPart LinkRequest linkRequest,
                                           @RequestPart MultipartFile image) throws IOException {
        return ResponseEntity.ok()
                .body(linkService.updateLink(id, linkRequest, image));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteLink(@PathVariable String id) {
        linkService.deleteLink(id);
        return ResponseEntity.accepted().build();
    }
}
