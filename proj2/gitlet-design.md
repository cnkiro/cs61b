# Gitlet Design Document

**Name**: cnkiro

## Classes and Data Structures

### Class 1 Commit

* Description: 每次commit的时候都创建一个commit对象，用来存储相关的信息

#### Fields

1. String ID: 每个commit对象都会有一个独特的ID用于区分
2. String message: 记录commit时提供的信息
3. Map<String, String> fileNameToBlobID
4. Date currentTime
5. List<String> parentsID

### Class 2 Blob

* Description: 每次add一个文件就会创建一个blob对象，用来存储文件的相关信息

#### Fields

1. String fileName
2. String ID
3. byte[] content

### Class 3 AddStage

* Description: 用于修改commit的blob暂存区

#### Fields

1. Map<String, String> fileNameToBlobID;

### Class 4 RemoveStage

* Description: 用于记录要删除的blob

#### Fields

1. Map<String, String> fileNameToBlobID

## Algorithms

## Persistence

