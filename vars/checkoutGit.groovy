def checkoutGit(pat) {
    sh """
        git clone -b main https://${pat}@github.com/Observe-Life-AI/ol-services-node.git
        git clone -b feature/OLMS-54-initial-node-container https://${pat}@github.com/Observe-Life-AI/ol-container-images-node.git
    """
}
