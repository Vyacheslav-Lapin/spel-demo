# SpEL Demo
Some Spring expression language (SpEL) expressions execution examples, taken from [this Josh Long video](https://youtu.be/0uvQQuxyAv4).

## Delombok
Unfortunately, Lombok-maven-plugin isn't work for Lombok 1.18.24+, so we need to use antrun-plugin for that procedure now...  

Principles of organizing build for create delomboked sources.jar, is described in [this article](https://habr.com/ru/companies/sberbank/articles/438548/). 

So, for full-delombok, you can type:  
```shell
$ mvn clean package -P=build
```
After that you can find delomboked code in `${project.baseDir}/target/generated-sources/delomboked`.
