def cleanWorkspace = {
    sh "rm -rf *"
}


node {
    stage "Clean workspace"
    cleanWorkspace()

    stage "Checkout"
    checkout scm

    withCredentials([[$class: 'FileBinding', credentialsId: 'NEXUS_CREDENTIALS', variable: 'NEXUS_CREDENTIALS']]) {
        docker.withRegistry("https://registry.grandata.com", "DOCKER_REGISTRY_CREDENTIALS") {
            docker.image('registry.grandata.com/grandata/minimal-java8-lzo-for-test:latest').inside {
                // Must do because build.sbt expect credentials on ~/.ivy2/.credential.
                // Should probably receive credentials file by argument.
                stage "Build"
                sh "mkdir -p /tmp/.ivy2"
                sh "cp \"\$NEXUS_CREDENTIALS\" /tmp/.ivy2/.credentials"
                sh "sbt -Duser.home=/tmp +test"

                if (env.BRANCH_NAME != 'master') {
                    return
                }

                stage "Publish"
                sh "sbt -Duser.home=/tmp +publish"
            }
        }
    }
}

