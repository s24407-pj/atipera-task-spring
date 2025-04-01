package org.demo.atiperataskspring.model.response;

import org.demo.atiperataskspring.model.Branch;

import java.util.List;

public record RepositoryResponse(
        String name,
        String owner,
        List<Branch> branches
) {
}
