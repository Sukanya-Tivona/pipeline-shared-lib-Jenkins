def call(String IMAGEVERSION, String projectname){
    
    def IMAGENAMES = ['data-read', 'data-write', 'timelines']
    for (IMAGENAME in IMAGENAMES)
    {
      sh "cp $HOME/workspace/$projectname/ol-container-images-node/Dockerfile $HOME/workspace/$projectname/ol-services-node/ol-node-api-$IMAGENAME"
        dir("$HOME/workspace/$projectname/ol-services-node/ol-node-api-${IMAGENAME}")
        {
        sh '''
        docker  build -t ${REPOSITORY_URI}/${IMAGENAME}:${IMAGEVERSION} .
        docker  push ${REPOSITORY_URI}/${IMAGENAME}:${IMAGEVERSION} '''
        }
        
    }
    
}





 
       
