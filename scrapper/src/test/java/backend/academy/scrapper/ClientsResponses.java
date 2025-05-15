package backend.academy.scrapper;

public class ClientsResponses {
    public static final String githubApiResponseWithTwoIssues =
            """
            [
              {
                "url": "https://api.github.com/repos/rustutam/TestRepo/issues/8",
                "repository_url": "https://api.github.com/repos/rustutam/TestRepo",
                "labels_url": "https://api.github.com/repos/rustutam/TestRepo/issues/8/labels{/name}",
                "comments_url": "https://api.github.com/repos/rustutam/TestRepo/issues/8/comments",
                "events_url": "https://api.github.com/repos/rustutam/TestRepo/issues/8/events",
                "html_url": "https://github.com/rustutam/TestRepo/issues/8",
                "id": 3066354308,
                "node_id": "I_kwDOOFKqos62xNqE",
                "number": 8,
                "title": "2",
                "user": {
                  "login": "rustutam",
                  "id": 113977718,
                  "node_id": "U_kgDOBsspdg",
                  "avatar_url": "https://avatars.githubusercontent.com/u/113977718?v=4",
                  "gravatar_id": "",
                  "url": "https://api.github.com/users/rustutam",
                  "html_url": "https://github.com/rustutam",
                  "followers_url": "https://api.github.com/users/rustutam/followers",
                  "following_url": "https://api.github.com/users/rustutam/following{/other_user}",
                  "gists_url": "https://api.github.com/users/rustutam/gists{/gist_id}",
                  "starred_url": "https://api.github.com/users/rustutam/starred{/owner}{/repo}",
                  "subscriptions_url": "https://api.github.com/users/rustutam/subscriptions",
                  "organizations_url": "https://api.github.com/users/rustutam/orgs",
                  "repos_url": "https://api.github.com/users/rustutam/repos",
                  "events_url": "https://api.github.com/users/rustutam/events{/privacy}",
                  "received_events_url": "https://api.github.com/users/rustutam/received_events",
                  "type": "User",
                  "user_view_type": "public",
                  "site_admin": false
                },
                "labels": [

                ],
                "state": "open",
                "locked": false,
                "assignee": null,
                "assignees": [

                ],
                "milestone": null,
                "comments": 0,
                "created_at": "2025-05-15T14:00:03Z",
                "updated_at": "2025-05-15T14:00:03Z",
                "closed_at": null,
                "author_association": "OWNER",
                "active_lock_reason": null,
                "sub_issues_summary": {
                  "total": 0,
                  "completed": 0,
                  "percent_completed": 0
                },
                "body": "2",
                "closed_by": null,
                "reactions": {
                  "url": "https://api.github.com/repos/rustutam/TestRepo/issues/8/reactions",
                  "total_count": 0,
                  "+1": 0,
                  "-1": 0,
                  "laugh": 0,
                  "hooray": 0,
                  "confused": 0,
                  "heart": 0,
                  "rocket": 0,
                  "eyes": 0
                },
                "timeline_url": "https://api.github.com/repos/rustutam/TestRepo/issues/8/timeline",
                "performed_via_github_app": null,
                "state_reason": null
              },
              {
                "url": "https://api.github.com/repos/rustutam/TestRepo/issues/7",
                "repository_url": "https://api.github.com/repos/rustutam/TestRepo",
                "labels_url": "https://api.github.com/repos/rustutam/TestRepo/issues/7/labels{/name}",
                "comments_url": "https://api.github.com/repos/rustutam/TestRepo/issues/7/comments",
                "events_url": "https://api.github.com/repos/rustutam/TestRepo/issues/7/events",
                "html_url": "https://github.com/rustutam/TestRepo/issues/7",
                "id": 3066354072,
                "node_id": "I_kwDOOFKqos62xNmY",
                "number": 7,
                "title": "11",
                "user": {
                  "login": "rustutam",
                  "id": 113977718,
                  "node_id": "U_kgDOBsspdg",
                  "avatar_url": "https://avatars.githubusercontent.com/u/113977718?v=4",
                  "gravatar_id": "",
                  "url": "https://api.github.com/users/rustutam",
                  "html_url": "https://github.com/rustutam",
                  "followers_url": "https://api.github.com/users/rustutam/followers",
                  "following_url": "https://api.github.com/users/rustutam/following{/other_user}",
                  "gists_url": "https://api.github.com/users/rustutam/gists{/gist_id}",
                  "starred_url": "https://api.github.com/users/rustutam/starred{/owner}{/repo}",
                  "subscriptions_url": "https://api.github.com/users/rustutam/subscriptions",
                  "organizations_url": "https://api.github.com/users/rustutam/orgs",
                  "repos_url": "https://api.github.com/users/rustutam/repos",
                  "events_url": "https://api.github.com/users/rustutam/events{/privacy}",
                  "received_events_url": "https://api.github.com/users/rustutam/received_events",
                  "type": "User",
                  "user_view_type": "public",
                  "site_admin": false
                },
                "labels": [

                ],
                "state": "open",
                "locked": false,
                "assignee": null,
                "assignees": [

                ],
                "milestone": null,
                "comments": 0,
                "created_at": "2025-05-15T14:00:00Z",
                "updated_at": "2025-05-15T14:00:00Z",
                "closed_at": null,
                "author_association": "OWNER",
                "active_lock_reason": null,
                "sub_issues_summary": {
                  "total": 0,
                  "completed": 0,
                  "percent_completed": 0
                },
                "body": "11",
                "closed_by": null,
                "reactions": {
                  "url": "https://api.github.com/repos/rustutam/TestRepo/issues/7/reactions",
                  "total_count": 0,
                  "+1": 0,
                  "-1": 0,
                  "laugh": 0,
                  "hooray": 0,
                  "confused": 0,
                  "heart": 0,
                  "rocket": 0,
                  "eyes": 0
                },
                "timeline_url": "https://api.github.com/repos/rustutam/TestRepo/issues/7/timeline",
                "performed_via_github_app": null,
                "state_reason": null
              }
              ]
            """;

    public static final String githubApiResponseJsonString =
            """
            [
              {
                "url": "https://api.github.com/repos/rustutam/TestRepo/issues/3",
                "repository_url": "https://api.github.com/repos/rustutam/TestRepo",
                "labels_url": "https://api.github.com/repos/rustutam/TestRepo/issues/3/labels{/name}",
                "comments_url": "https://api.github.com/repos/rustutam/TestRepo/issues/3/comments",
                "events_url": "https://api.github.com/repos/rustutam/TestRepo/issues/3/events",
                "html_url": "https://github.com/rustutam/TestRepo/issues/3",
                "id": 3027526247,
                "node_id": "I_kwDOOFKqos60dGJn",
                "number": 3,
                "title": "1233434234",
                "user": {
                  "login": "rustutam",
                  "id": 113977718,
                  "node_id": "U_kgDOBsspdg",
                  "avatar_url": "https://avatars.githubusercontent.com/u/113977718?v=4",
                  "gravatar_id": "",
                  "url": "https://api.github.com/users/rustutam",
                  "html_url": "https://github.com/rustutam",
                  "followers_url": "https://api.github.com/users/rustutam/followers",
                  "following_url": "https://api.github.com/users/rustutam/following{/other_user}",
                  "gists_url": "https://api.github.com/users/rustutam/gists{/gist_id}",
                  "starred_url": "https://api.github.com/users/rustutam/starred{/owner}{/repo}",
                  "subscriptions_url": "https://api.github.com/users/rustutam/subscriptions",
                  "organizations_url": "https://api.github.com/users/rustutam/orgs",
                  "repos_url": "https://api.github.com/users/rustutam/repos",
                  "events_url": "https://api.github.com/users/rustutam/events{/privacy}",
                  "received_events_url": "https://api.github.com/users/rustutam/received_events",
                  "type": "User",
                  "user_view_type": "public",
                  "site_admin": false
                },
                "labels": [

                ],
                "state": "open",
                "locked": false,
                "assignee": null,
                "assignees": [

                ],
                "milestone": null,
                "comments": 0,
                "created_at": "2025-04-29T08:40:47Z",
                "updated_at": "2025-04-29T08:40:47Z",
                "closed_at": null,
                "author_association": "OWNER",
                "active_lock_reason": null,
                "sub_issues_summary": {
                  "total": 0,
                  "completed": 0,
                  "percent_completed": 0
                },
                "body": "4324234324",
                "closed_by": null,
                "reactions": {
                  "url": "https://api.github.com/repos/rustutam/TestRepo/issues/3/reactions",
                  "total_count": 0,
                  "+1": 0,
                  "-1": 0,
                  "laugh": 0,
                  "hooray": 0,
                  "confused": 0,
                  "heart": 0,
                  "rocket": 0,
                  "eyes": 0
                },
                "timeline_url": "https://api.github.com/repos/rustutam/TestRepo/issues/3/timeline",
                "performed_via_github_app": null,
                "state_reason": null
              },
              {
                "url": "https://api.github.com/repos/rustutam/TestRepo/issues/2",
                "repository_url": "https://api.github.com/repos/rustutam/TestRepo",
                "labels_url": "https://api.github.com/repos/rustutam/TestRepo/issues/2/labels{/name}",
                "comments_url": "https://api.github.com/repos/rustutam/TestRepo/issues/2/comments",
                "events_url": "https://api.github.com/repos/rustutam/TestRepo/issues/2/events",
                "html_url": "https://github.com/rustutam/TestRepo/issues/2",
                "id": 3025426833,
                "node_id": "I_kwDOOFKqos60VFmR",
                "number": 2,
                "title": "wewewq",
                "user": {
                  "login": "rustutam",
                  "id": 113977718,
                  "node_id": "U_kgDOBsspdg",
                  "avatar_url": "https://avatars.githubusercontent.com/u/113977718?v=4",
                  "gravatar_id": "",
                  "url": "https://api.github.com/users/rustutam",
                  "html_url": "https://github.com/rustutam",
                  "followers_url": "https://api.github.com/users/rustutam/followers",
                  "following_url": "https://api.github.com/users/rustutam/following{/other_user}",
                  "gists_url": "https://api.github.com/users/rustutam/gists{/gist_id}",
                  "starred_url": "https://api.github.com/users/rustutam/starred{/owner}{/repo}",
                  "subscriptions_url": "https://api.github.com/users/rustutam/subscriptions",
                  "organizations_url": "https://api.github.com/users/rustutam/orgs",
                  "repos_url": "https://api.github.com/users/rustutam/repos",
                  "events_url": "https://api.github.com/users/rustutam/events{/privacy}",
                  "received_events_url": "https://api.github.com/users/rustutam/received_events",
                  "type": "User",
                  "user_view_type": "public",
                  "site_admin": false
                },
                "labels": [

                ],
                "state": "open",
                "locked": false,
                "assignee": null,
                "assignees": [

                ],
                "milestone": null,
                "comments": 0,
                "created_at": "2025-04-28T15:58:24Z",
                "updated_at": "2025-04-28T15:58:24Z",
                "closed_at": null,
                "author_association": "OWNER",
                "active_lock_reason": null,
                "sub_issues_summary": {
                  "total": 0,
                  "completed": 0,
                  "percent_completed": 0
                },
                "body": "ewqewqewq",
                "closed_by": null,
                "reactions": {
                  "url": "https://api.github.com/repos/rustutam/TestRepo/issues/2/reactions",
                  "total_count": 0,
                  "+1": 0,
                  "-1": 0,
                  "laugh": 0,
                  "hooray": 0,
                  "confused": 0,
                  "heart": 0,
                  "rocket": 0,
                  "eyes": 0
                },
                "timeline_url": "https://api.github.com/repos/rustutam/TestRepo/issues/2/timeline",
                "performed_via_github_app": null,
                "state_reason": null
              },
              {
                "url": "https://api.github.com/repos/rustutam/TestRepo/issues/1",
                "repository_url": "https://api.github.com/repos/rustutam/TestRepo",
                "labels_url": "https://api.github.com/repos/rustutam/TestRepo/issues/1/labels{/name}",
                "comments_url": "https://api.github.com/repos/rustutam/TestRepo/issues/1/comments",
                "events_url": "https://api.github.com/repos/rustutam/TestRepo/issues/1/events",
                "html_url": "https://github.com/rustutam/TestRepo/issues/1",
                "id": 2964175381,
                "node_id": "I_kwDOOFKqos6wrboV",
                "number": 1,
                "title": "ывывывы",
                "user": {
                  "login": "rustutam",
                  "id": 113977718,
                  "node_id": "U_kgDOBsspdg",
                  "avatar_url": "https://avatars.githubusercontent.com/u/113977718?v=4",
                  "gravatar_id": "",
                  "url": "https://api.github.com/users/rustutam",
                  "html_url": "https://github.com/rustutam",
                  "followers_url": "https://api.github.com/users/rustutam/followers",
                  "following_url": "https://api.github.com/users/rustutam/following{/other_user}",
                  "gists_url": "https://api.github.com/users/rustutam/gists{/gist_id}",
                  "starred_url": "https://api.github.com/users/rustutam/starred{/owner}{/repo}",
                  "subscriptions_url": "https://api.github.com/users/rustutam/subscriptions",
                  "organizations_url": "https://api.github.com/users/rustutam/orgs",
                  "repos_url": "https://api.github.com/users/rustutam/repos",
                  "events_url": "https://api.github.com/users/rustutam/events{/privacy}",
                  "received_events_url": "https://api.github.com/users/rustutam/received_events",
                  "type": "User",
                  "user_view_type": "public",
                  "site_admin": false
                },
                "labels": [

                ],
                "state": "open",
                "locked": false,
                "assignee": null,
                "assignees": [

                ],
                "milestone": null,
                "comments": 3,
                "created_at": "2025-04-01T17:56:11Z",
                "updated_at": "2025-04-01T17:56:32Z",
                "closed_at": null,
                "author_association": "OWNER",
                "active_lock_reason": null,
                "sub_issues_summary": {
                  "total": 0,
                  "completed": 0,
                  "percent_completed": 0
                },
                "body": "ыфвыфв",
                "closed_by": null,
                "reactions": {
                  "url": "https://api.github.com/repos/rustutam/TestRepo/issues/1/reactions",
                  "total_count": 0,
                  "+1": 0,
                  "-1": 0,
                  "laugh": 0,
                  "hooray": 0,
                  "confused": 0,
                  "heart": 0,
                  "rocket": 0,
                  "eyes": 0
                },
                "timeline_url": "https://api.github.com/repos/rustutam/TestRepo/issues/1/timeline",
                "performed_via_github_app": null,
                "state_reason": null
              }
            ]
            """;
}
