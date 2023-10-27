def awsConfigureAndEcrLogin(params) {
    sh """
        aws configure set aws_access_key_id ${params.awsAccessKeyId}
        aws configure set aws_secret_access_key ${params.awsSecretAccessKey}
        aws configure set default.region ${params.awsRegion}
        aws ecr get-login-password --region ${params.awsRegion} | docker login --username AWS --password-stdin ${params.awsAccountId}.dkr.ecr.${params.awsRegion}.amazonaws.com
    """
}
