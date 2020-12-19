Made by [Antino Labs](https://www.antino.io/) with ❤️


# wddOnboardingLibrary
![Screenshot_2020-06-09-12-18-25-23_28d2e796c586c3c535ec0c82b0c38245](https://user-images.githubusercontent.com/51435895/84117013-82643e00-aa4e-11ea-8b20-772366fdaf7f.jpg)  ![Screenshot_2020-06-09-12-18-29-97_28d2e796c586c3c535ec0c82b0c38245](https://user-images.githubusercontent.com/51435895/84117535-4bdaf300-aa4f-11ea-936f-62bf72bae6ef.jpg)  ![Screenshot_2020-06-09-12-18-34-68_28d2e796c586c3c535ec0c82b0c38245](https://user-images.githubusercontent.com/51435895/84118436-a032a280-aa50-11ea-832d-3f9aa2dfe9f2.jpg)    ![Screenshot_2020-06-09-12-18-45-40_28d2e796c586c3c535ec0c82b0c38245](https://user-images.githubusercontent.com/51435895/84118066-197dc580-aa50-11ea-9a25-481e10018330.jpg)


An Android Library written in Java, for Recognition of an Image 

[![Android API23+](https://img.shields.io/badge/Android-API_23+-green.svg)]()
  [![Java 6+](https://img.shields.io/badge/Java-6+-red.svg)]()
  
  
  
  ## GuideLines 
  If you want to use our Aws crediential. in case you dont have aws account connect with [Antino Labs](https://www.antino.io/)
  							or						
  If you have an account you want to setup our Aws crediential then follow these steps( **Mandatory currently this time is only Working Us-east-1 Region**)
  
  ## Steps for Setups
  
  ## 1.1 Create Cognito pool
  
   We will create an Identity Pool in AWS Cognito, for this we go to the AWS Cognito administration panel and click on Manage     Identity Pools.
  
 We will create an Identity Pool in AWS Cognito, for this we go to the AWS Cognito administration panel and click on Manage Identity Pools.
 
 ![cognitio](https://user-images.githubusercontent.com/51435895/84233522-1cd88600-ab10-11ea-8acb-fb1a57c77bfa.png)
 
 
 Click Create new Identity pool button on the top left of the console.
Give a name for the Identity pool and check Enable access to unauthenticated identities under the Unauthenticated Identities section, click Create pool button on the bottom right.
We will call “tutorialpool” and check the box “Enable access to unauthenticated identities”

![createpool](https://user-images.githubusercontent.com/51435895/84233732-86589480-ab10-11ea-961b-9df5d7e8254c.png)

Click in Create Pool
To enable Cognito Identities to access your resources, expand the View Details section to see the two roles that are to be created. Make a note of the unauth role whose name is of the form Cognito_<IdentityPoolName>Unauth_Role.
We take note of the name that the unauthenticated identities that would like to access Cognito will use. In this case the name is “Cognito_tutorialpoolUnauth_Role”
	
![document_details](https://user-images.githubusercontent.com/51435895/84233917-e9e2c200-ab10-11ea-8acc-5f897a1aa455.png)

Now click Allow button in the bottom right of the console.
Under Get AWSCredentials section, in the code snippet to create CognitoCachingCredentialsProvider , find the Identity pool ID and the AWS region and make note of them. You will need to add to the sample application later.

![credential](https://user-images.githubusercontent.com/51435895/84234093-35956b80-ab11-11ea-8faf-7db1585c0e81.png)

Copy Identity pool ID: “us-east-1:kjdfkjdfbkjdsbdsfkjdskjdfsbkjfdabnkjfdgngf”
and regions: “Regions.US_EAST_1”, we will need them later. Finally we press on Go To Dashboard.

## 1.2 Setup Permission

Go to Amazon IAM Console and select “Roles”.
Select the unauth role you just created in step 1, which is of the form Cognito_<IdentityPoolName>Unauth_Role.

![permisson1](https://user-images.githubusercontent.com/51435895/84234417-d552f980-ab11-11ea-955a-1adedc1f61cb.png)

Select Attach Policy, then find AmazonS3FullAccess, AmazonTextractFullAccess and Aws Rekogniton all are attached to the role.

![image](https://user-images.githubusercontent.com/51435895/84234707-4db9ba80-ab12-11ea-82ce-37d24e23ceaf.png)

## 1.3 Create Bucket

Go to Amazon S3 Console and click Create bucket.
Enter a name for the bucket that is DNS-compliant.
Choose the region that you want the bucket to be created.(**this library only support US-East-Region1**)
Click Create. Note the name and the region of the bucket that was created

![bucketname](https://user-images.githubusercontent.com/51435895/84235217-18fa3300-ab13-11ea-86e7-9d0106e2629b.png)

## Create IAM User for Aws Rekognition

Follow official doc to get the IAM user. (Link is given below)

https://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html 

And attach the same policy which are give to console pool.

## Installation

### gradle

Add it in your root build.gradle at the end of repositories:

```bash
allprojects {
  repositories {
	...
	maven { url 'https://jitpack.io' }
  }
}
```

Add dependency in your build.gradle.

```bash
	         'implementation 'com.github.antinolabs:Android_WDD_New:Tag'
```

## Permissions
Add permissions in your manifest.xml. Request permission from your app if you are using SDK Level 26+.

```bash
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

## Usage

	1.  Create the instance  of a CustomButton class
	2.  Connected with the parent layout.
	3.  Call the method of the custom layout and pass the parameters accessKey and secretKey
	4   Add the view with customLayout.

```java
       CustomButton relativeLayout = new CustomButton(MainActivity.this);
       CustomButton.KeyAccess(AccessKey,SecretKey,CognitoPoolId,BucketName);
       ConstraintLayout constraintLayout =  findViewById(R.id.parent);
       relativeLayout.init();
       constraintLayout.addView(relativeLayout);
 ```
 
 ## Contributing
 [Antino Labs](https://www.antino.io/)
- If you **need help** or you'd like to **ask a general question**
- If you **found a bug**, open a service request.
- If you **have a feature request**, open a service request.
- If you **want to contribute**, submit a pull request.
 
 ## License
[MIT](https://choosealicense.com/licenses/mit/)
