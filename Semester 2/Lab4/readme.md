Build app and add docker image with app to local docker registry:

```mvn -am -pl client-app package```


```mvn -am -pl stock-app package```

Run integration test with docker:

```mvn -am -pl test-example test```