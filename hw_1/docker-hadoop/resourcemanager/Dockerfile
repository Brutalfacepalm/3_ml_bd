FROM bde2020/hadoop-base:2.0.0-hadoop3.2.1-java8

RUN apt update && \
    apt install -y wget build-essential checkinstall \
    build-essential checkinstall libreadline-gplv2-dev libncursesw5-dev \
    libssl-dev libsqlite3-dev tk-dev libgdbm-dev libc6-dev libbz2-dev libffi-dev zlib1g-dev python3-tk

ARG PYTHON_MAJOR=3.8

ARG PYTHON_MINOR=9

ARG PYTHON_VERSION=${PYTHON_MAJOR}.${PYTHON_MINOR}

RUN mkdir -p /opt/python

RUN wget https://www.python.org/ftp/python/${PYTHON_VERSION}/Python-${PYTHON_VERSION}.tgz -O /opt/python/python.tgz && \
    cd /opt/python && \
    tar xzf python.tgz && \
    cd ./Python-${PYTHON_VERSION} && \
    ./configure --enable-optimizations && \
    make -j $(nproc) altinstall && \
    rm -r /opt/python

RUN update-alternatives --install /usr/bin/python python /usr/local/bin/python${PYTHON_MAJOR} 1


MAINTAINER Ivan Ermilov <ivan.s.ermilov@gmail.com>

HEALTHCHECK CMD curl -f http://localhost:8088/ || exit 1

ADD run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 8088

CMD ["/run.sh"]
