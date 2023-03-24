set -e
# CI流程第一步，生成jar或其他可执行二进制文件，jar会被下一步镜像构建时使用，具体可见Dockerfile

# shellcheck disable=SC2068
echo $@
echo $1
echo $2

cd ../

mvn clean install -e -U -Dmaven.test.skip=true

mv target/boot-admin-0.0.1-SNAPSHOT.tar.gz  k8s-build/boot-admin.tar.gz