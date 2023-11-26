# LearnApp
## Content navigation:
- [Description](#Description)
- [Link to Design](#link-to-design)
- [Architecture](#Architecture)
- [Technologies](#technologies)
- [Installation and Running](#installation-and-running)

# Description
## General Features

- **Registration**: Join our platform as a trainer or student by creating an account with minimal effort.
- **Profile**: Personalize your profile, add photos, specify skills, and showcase achievements.
- **Statistics**: Track your progress and achievements using convenient statistical tools.
- **Communities**: Communicate with other participants, create communities, and share experiences.
- **Change Password**: Easily update your account password for enhanced security.
  
## Student Features
- **Become a Student**: Register as a student to study and develop your knowledges in a specific area.
- **Enroll in Studing**: Join interesting lessons conducted by trainers to enhance your knowledge.
- **Lessons**: Create and edit lessons, manage schedules, and share information with participants.
- **Participate in Communities**: Engage with other students in communities, sharing insights and experiences.

## Trainer Features

- **Become a Trainer**: Register as a trainer to share your expertise and knowledge.
- **Conduct Lessons**: Organize and conduct lessons with your students to facilitate learning.
  
## Link to Design
[https://app.visily.ai/projects/2c6d2ce8-3612-4175-9e38-2402ef2e3703/boards/514811](https://app.visily.ai/projects/2c6d2ce8-3612-4175-9e38-2402ef2e3703/boards/514811)<br><br>
# Architecture
This application is based on microservice architecture.
- main microservice with main logic layer
- report microservice for trainer workload calculation and generating reports using AWS Lambda 
### On premise version
![image](https://github.com/MaksymKhimii/LearnApp/assets/94639350/6bcaf1a6-3eda-4786-81f1-c0c9e87fd367)
### Cloud version with AWS infrastructure
![image](https://github.com/MaksymKhimii/LearnApp/assets/94639350/00d1d997-3436-428f-a030-4af30cc7231e)

# Technologies
## Backend & Cloud technologies:
- Java 17
- Spring (Boot, MVC, Data, Cloud, Security)
- Hibernate
- PostgresSQL
- MongoDB (DynamoDB in cloud version)
- ActiveMQ (SQS in cloud version)
- AWS (S3, EC2, VPC, Lambda, DynamoDB, RDS, SQS, CloudFront, CloudWatch)
- Docker
- Jenkins
## Frontend technologies:
- Angular
- JavaScript
- TypeScript
- HTML
- CSS
- Bootstrap
# Installation and Running

1. Clone the repository.
2. Install the necessary dependencies.
3. Run the application.

