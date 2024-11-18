# DEVELOPER

## How to install a mongodb in the local computer

### Install the mongodb

Here is link to install the mongodb
- https://www.mongodb.com/try/download/community

### Install the mongodb GUI

We use mongodb compass, please install from here
- https://www.mongodb.com/products/tools/compass

### Set up and Start the mongodb

Place both of mongod and mongos in your bin directory like here.

```shell
mv Downloads/.../bin/mongod /usr/local/bin/
mv Downloads/.../bin/mongos /usr/local/bin/
```

Execute the command below to start mongod with dbpath.

```shell
mongod --dbpath /usr/local/var/mongodb
```
