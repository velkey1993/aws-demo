## Deployment Steps
* Create _**JAR**_ file: `mvn clean install`
* Upload _**JAR**_ file to _**S3**_ bucket: `upload_jar_to_s3`
* _(Optional)_ Create _**VPC** (Virtual Private Cloud)_ with _**Subnet**s_. Set up related _**SG**s (Security Group)_.
* Create _**AMI** (Amazon Machine Image)_ with updated _**Java**_ version and necessary _**IAM**_ role _(EC2 => S3 - Read Only)_: `sudo yum install java-1.8.0`
* Create _**ASG** (Auto Scaling Group)_ with _**ASLC** (Auto Scaling Launch Configuration)_.
* Set _**User data**_ in _**ASLC** (Auto Scaling Launch Configuration)_:
```
Content-Type: multipart/mixed; boundary="//"
MIME-Version: 1.0


--//
Content-Type: text/cloud-config; charset="us-ascii"
MIME-Version: 1.0
Content-Transfer-Encoding: 7bit
Content-Disposition: attachment; filename="cloud-config.txt"


#cloud-config
cloud_final_modules:
- [scripts-user, always]


--//
Content-Type: text/x-shellscript; charset="us-ascii"
MIME-Version: 1.0
Content-Transfer-Encoding: 7bit
Content-Disposition: attachment; filename="userdata.txt"


#!/bin/bash
cd /home/ec2-user/
aws s3 cp s3://link-jar-bucket/links-0.0.1-SNAPSHOT.jar .
java -jar links-0.0.1-SNAPSHOT.jar
--//
```

* Create _**LB** (Load Balancer)_ and _**LBTG** (Load Balancing Target Group)_.
* ...
