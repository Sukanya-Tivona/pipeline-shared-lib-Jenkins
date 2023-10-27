#!/usr/bin/env groovy
def call(String repo, String branch='master') {
  echo "||------ CLONING $repo ------||"
  
  git branch: branch, changelog: false, credentialsId: 'jenkinsgit', poll: false, url: "git@github.com:myprivateorg/$repo.git"
}

return this
