def call() {
    pipeline{
      environment
     {
      
      PAT = credentials('PAT')
    
      }
      agent any
    stages 
    {
       stage('Checkout') 
       {
       steps {
           
           git branch: 'main', credentialsId: 'My_Github_Tivona_login', url: 'https://$PAT@github.com/Observe-Life-AI/ol-services-node.git'
          
           git branch: 'feature', credentialsId: 'My_Github_Tivona_login', url: 'https://$PAT@github.com/Observe-Life-AI/ol-container-images-node.git'
           
          }
       }
    }
    }
}
        
     
