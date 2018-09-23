all: gradle

gradle:
	@rm -rf .gradle
	@gradle clean build
