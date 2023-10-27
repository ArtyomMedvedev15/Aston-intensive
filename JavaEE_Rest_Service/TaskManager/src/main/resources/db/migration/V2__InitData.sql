INSERT INTO taskmaneger.users
(id, username,email)
SELECT 777, 'TestUsr','test@mail.com'
    WHERE
    NOT EXISTS (
        SELECT id FROM taskmaneger.users WHERE id = 777
    );

INSERT INTO taskmaneger.users
(id, username,email)
SELECT 778, 'TestUsr','test@mail.com'
    WHERE
    NOT EXISTS (
        SELECT id FROM taskmaneger.users WHERE id = 778
    );

INSERT INTO taskmaneger.users
(id, username,email)
SELECT 779, 'DeleteUser','test@mail.com'
    WHERE
    NOT EXISTS (
        SELECT id FROM taskmaneger.users WHERE id = 779
    );

INSERT INTO taskmaneger.project
(id, name,description)
SELECT 777, 'TestProject1','TestProject'
    WHERE
    NOT EXISTS (
        SELECT id FROM taskmaneger.project WHERE id = 777
    );

INSERT INTO taskmaneger.project
(id, name,description)
SELECT 778, 'TestProject2','TestProject'
    WHERE
    NOT EXISTS (
        SELECT id FROM taskmaneger.project WHERE id = 778
    );

INSERT INTO taskmaneger.project
(id, name,description)
SELECT 779, 'TestProject3','TestProject'
    WHERE
    NOT EXISTS (
        SELECT id FROM taskmaneger.project WHERE id = 779
    );

INSERT INTO taskmaneger.task
    (id,title,description,deadline,status,projectId)
SELECT 779, 'TestTask1','TestTask1','2023-10-27','Open',777
    WHERE
    NOT EXISTS (
        SELECT id FROM taskmaneger.task WHERE id = 779
    );

INSERT INTO taskmaneger.task
(id,title,description,deadline,status,projectId)
SELECT 778, 'TestTask2','TestTask2','2023-10-27','Open',778
    WHERE
    NOT EXISTS (
        SELECT id FROM taskmaneger.task WHERE id = 778
    );

INSERT INTO taskmaneger.task
(id,title,description,deadline,status,projectId)
SELECT 779, 'TestTask3','TestTask3','2023-10-27','Open',779
    WHERE
    NOT EXISTS (
        SELECT id FROM taskmaneger.task WHERE id = 779
    );

INSERT INTO taskmaneger.usertask
(id,userid,taskid)
SELECT 777,777,779
    WHERE
    NOT EXISTS (
        SELECT id FROM taskmaneger.usertask WHERE id = 777
    );

INSERT INTO taskmaneger.usertask
(id, userid,taskid)
SELECT 778,778,778
    WHERE
    NOT EXISTS (
        SELECT id FROM taskmaneger.usertask WHERE id = 778
    );