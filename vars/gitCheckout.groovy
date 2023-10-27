def call(def repo, def branchName){

    script{
        dir(repo) {
            git branch: "${branchName}",
                    credentialsId: 'My_Github_Tivona_login',
                    url: "https://github.com/Sukanya-Tivona/helm_jenkins.git"
        }
    }

}
