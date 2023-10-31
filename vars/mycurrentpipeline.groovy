def call(Map pipelineParams = [:]) {
pipeline {
environment {
aws_account_id= credentials('AWS_ACCOUNT_ID')
aws_default_region="us-west-2"
AWS_ACCESS_KEY_ID     = credentials('aws_pratice')
AWS_SECRET_ACCESS_KEY = credentials('aws_pratice')
PAT = credentials('PAT')
repository_uri = "${aws_account_id}.dkr.ecr.${aws_default_region}.amazonaws.com"
}
    agent any							
    parameters {							
       string(name: 'image_version', defaultValue: '', description: 'image version to be build')
      // string(name: 'aws_default_region', defaultValue: 'us-west-2', description: 'region name')
      // extendedChoice multiSelectDelimiter: ',', name: 'image_names', quoteValue: false, saveJSONParameterToFile: false, type: 'PT_CHECKBOX', value: 'data-read,data-write,timelines', visibleItemCount: 3
       }							
   	
    stages
    {
       stage('Checkout')
       {
       steps {
          sh '''
              git clone -b main https://$PAT@github.com/Observe-Life-AI/ol-services-node.git
              git clone -b feature/OLMS-54-initial-node-container https://$PAT@github.com/Observe-Life-AI/ol-container-images-node.git
          '''
               }
       }
       stage('AWS configure and ecr login ')
       {
       steps { sh '''
              
              aws configure set aws_access_key_id $AWS_ACCESS_KEY_ID
              aws configure set aws_secret_access_key $AWS_SECRET_ACCESS_KEY
              aws configure set default.region $aws_default_region
       
              aws ecr get-login-password --region ${aws_default_region} | docker login --username AWS --password-stdin ${aws_account_id}.dkr.ecr.${aws_default_region}.amazonaws.com
               '''
              }
       }
       //locally run
       //sudo chmod 777 /var/run/docker.sock
       stage('Build and Push Docker Images')
       {
       steps
       {
       script
       {
            //   List arr = image_names.split(",")
            //   def image_names = arr
            def image_names = ['data-read', 'data-write', 'timelines']
             for (image_name in image_names)
              {
                sh "cp $HOME/workspace/$env.JOB_NAME/ol-container-images-node/Dockerfile $HOME/workspace/$env.JOB_NAME/ol-services-node/ol-node-api-$image_name"
                  dir("$HOME/workspace/$env.JOB_NAME/ol-services-node/ol-node-api-${image_name}")
                  {
                      sh "docker build -t ${repository_uri}/${image_name}:${image_version} ."
                    
                      sh "docker push ${repository_uri}/${image_name}:${image_version} "
                  }
              }
       }
       }
       }
    }
  // post build cleanup
    post {
        cleanup {
            echo "Clean up in post work space"
            cleanWs()
        }
    }
}
}

