  def call(Map params) {							
       def IMAGEVERSION = params.IMAGEVERSION ?: '1.0'
       def projectname = params.projectname ?:'yourpipelinename'
       def AWS_DEFAULT_REGION = params.AWS_DEFAULT_REGION ?:'us-west-2'
       def AWS_ACCOUNT_ID = params.AWS_ACCOUNT_ID ?: '562922379100'
       return [IMAGEVERSION : IMAGEVERSION, projectname: projectname, AWS_DEFAULT_REGION :AWS_DEFAULT_REGION, AWS_ACCOUNT_ID :AWS_ACCOUNT_ID]
    
       }
