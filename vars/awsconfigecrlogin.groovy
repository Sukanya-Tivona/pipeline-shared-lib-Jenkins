def call(String aws_account_id, String aws_region, String AWS_ACCESS_KEY_ID, String AWS_SECRET_ACCESS_KEY){
    
    // sh """
    //  aws ecr get-login-password --region ${region} | docker login --username AWS --password-stdin ${aws_account_id}.dkr.ecr.${region}.amazonaws.com
    //  docker push ${aws_account_id}.dkr.ecr.${region}.amazonaws.com/${ecr_repoName}:latest
    // "''
  sh '''
  aws configure set aws_access_key_id $AWS_ACCESS_KEY_ID
  aws configure set aws_secret_access_key $AWS_SECRET_ACCESS_KEY
  aws configure set default.region $aws_region
  aws ecr get-login-password --region ${aws_region} | 
  docker login --username AWS --password-stdin ${aws_account_id}.dkr.ecr.${aws_region}.amazonaws.com
   '''
}