INSERT INTO taskmanager.users
(id, username,email)
SELECT 777, 'TestUsr','test@mail.com'
    WHERE
    NOT EXISTS (
        SELECT id FROM taskmanager.users WHERE id = 777
    );

INSERT INTO taskmanager.users
(id, username,email)
SELECT 778, 'TestUsr','test@mail.com'
    WHERE
    NOT EXISTS (
        SELECT id FROM taskmanager.users WHERE id = 778
    );

INSERT INTO taskmanager.users
(id, username,email)
SELECT 779, 'DeleteUser','test@mail.com'
    WHERE
    NOT EXISTS (
        SELECT id FROM taskmanager.users WHERE id = 779
    );

INSERT INTO taskmanager.project
(id, name,description)
SELECT 777, 'TestProject1','TestProject'
    WHERE
    NOT EXISTS (
        SELECT id FROM taskmanager.project WHERE id = 777
    );

INSERT INTO taskmanager.project
(id, name,description)
SELECT 778, 'TestProject2','TestProject'
    WHERE
    NOT EXISTS (
        SELECT id FROM taskmanager.project WHERE id = 778
    );

INSERT INTO taskmanager.project
(id, name,description)
SELECT 779, 'TestProject3','TestProject'
    WHERE
    NOT EXISTS (
        SELECT id FROM taskmanager.project WHERE id = 779
    );

INSERT INTO taskmanager.task
    (id,title,description,deadline,status,project_id)
SELECT 779, 'TestTask1','TestTask1','2023-10-27','Open',777
    WHERE
    NOT EXISTS (
        SELECT id FROM taskmanager.task WHERE id = 779
    );

INSERT INTO taskmanager.task
(id,title,description,deadline,status,project_id)
SELECT 778, 'TestTask2','TestTask2','2023-10-27','Open',778
    WHERE
    NOT EXISTS (
        SELECT id FROM taskmanager.task WHERE id = 778
    );

INSERT INTO taskmanager.task
(id,title,description,deadline,status,project_id)
SELECT 779, 'TestTask3','TestTask3','2023-10-27','Open',779
    WHERE
    NOT EXISTS (
        SELECT id FROM taskmanager.task WHERE id = 779
    );

INSERT INTO taskmanager.usertask
(id,user_id,task_id)
SELECT 888,777,779
    WHERE
    NOT EXISTS (
        SELECT user_id FROM taskmanager.usertask WHERE user_id = 777
    );

INSERT INTO taskmanager.usertask
(id,user_id,task_id)
SELECT 777,778,778
    WHERE
    NOT EXISTS (
        SELECT user_id FROM taskmanager.usertask WHERE user_id = 778
    );