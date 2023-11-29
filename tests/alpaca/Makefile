BINARY_NAME=lottery-winners
MAIN_NAME=cmd/cli.go
DIST=./dist

run:
	go run ${MAIN_NAME} $(arg1)

build:
	GOARCH=amd64 GOOS=darwin go build -o ${DIST}-darwin/${BINARY_NAME} ${MAIN_NAME}
	GOARCH=amd64 GOOS=linux go build -o ${DIST}-linux/${BINARY_NAME} ${MAIN_NAME}
	GOARCH=amd64 GOOS=windows go build -o ${DIST}-windows/${BINARY_NAME} ${MAIN_NAME}

deps:
	@[ -d $(DIST)-linux ] || mkdir -p $(DIST)-linux
	@[ -d $(DIST)-darwin ] || mkdir -p $(DIST)-darwin
	@[ -d $(DIST)-windows ] || mkdir -p $(DIST)-windows

clean:
	@rm -rf ${DIST}*

all: deps build
