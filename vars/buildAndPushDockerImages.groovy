def buildAndPushDockerImages(params) {
    script {
        def IMAGENAMES = ['data-read', 'data-write', 'timelines']
        for (IMAGENAME in IMAGENAMES) {
            sh "cp $HOME/workspace/${params.projectname}/ol-container-images-node/Dockerfile $HOME/workspace/${params.projectname}/ol-services-node/ol-node-api-${IMAGENAME}"
            dir("$HOME/workspace/${params.projectname}/ol-services-node/ol-node-api-${IMAGENAME}") {
                sh "docker build -t ${params.repositoryUri}/${IMAGENAME}:${params.imageVersion} ."
                sh "docker push ${params.repositoryUri}/${IMAGENAME}:${params.imageVersion}"
            }
        }
    }
}
