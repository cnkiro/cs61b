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

### Class 5 Repository

* Description: 用于handle各种用户的输入

#### Fields

1. String 各种文件及文件夹的路径

## Algorithms

###  init 
1. Check the '.gitlet' directory is created or not. If not, follow the 2nd step. Otherwise, output some error message.
2. Build the whole directory according to the diagram above.
3. Make an initial commit

### add
1. Check the file is created or not. If not, output some error message. Otherwise, follow the 2nd step.
2. Create a new blob object, and add to the AddStage, write in the Addstage file(blob id) and Objects directory(a file named by the blob id, and the content is the hash value of blob).

### commit 
1. Copy the head commit's blobs.
2. If the AddStage and RemoveStage is empty, output some message. Otherwise, modify the blobs according to the AddStage and RemoveStage.
3. Create a new commit object, and save it to the Objects dir, and rewrite the HEAD file and correspond branch file.
4. Clear the AddStage and RemoveStage

### rm
* There are three different solution
1. the file is already added to the AddStage, then just delete correspond blob object(both in file and AddStage)
2. the file is traced by the headCommit and exists in the current working directory, then delete the file and create a new blob object correspond to the file, and add it to the RemoveStage.
3. Like 2nd, but the file doesn't exist in the directory, then just create blob object and put it to the RemoveStage.

### log
## Persistence

