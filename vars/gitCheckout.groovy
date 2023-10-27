def call(def repo, def branchName){

    script{
        dir(repo) {
            git branch: "${branchName}",
                    credentialsId: 'github-account',
                    url: "https://github.com/Observe-Life-AI/${repo}.git"
        }
    }

}
