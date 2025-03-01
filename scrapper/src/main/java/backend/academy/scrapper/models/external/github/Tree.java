package backend.academy.scrapper.models.external.github;

import java.net.URI;

record Tree(
    String sha,
    URI url
) {
}
