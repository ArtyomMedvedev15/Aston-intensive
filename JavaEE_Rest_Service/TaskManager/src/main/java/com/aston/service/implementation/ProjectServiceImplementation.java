package com.aston.service.implementation;

import com.aston.dao.api.ProjectDaoApi;
import com.aston.entities.Project;
import com.aston.service.api.ProjectServiceApi;
import com.aston.util.ProjectInvalidParameterException;
import com.aston.util.ProjectNotFoundException;
import com.aston.util.TransactionException;
import com.aston.util.dto.ProjectDto;
import com.aston.util.dto.ProjectUpdateDto;
import com.aston.util.dto.TaskDto;
import com.aston.util.dto.util.ProjectDtoUtil;
import com.aston.util.dto.util.TaskDtoUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.aston.util.dto.util.ProjectDtoUtil.*;

@Slf4j
public class ProjectServiceImplementation implements ProjectServiceApi {
    private final SessionFactory sessionFactory;
    private final ProjectDaoApi projectDaoApi;

    public ProjectServiceImplementation(ProjectDaoApi projectDaoApi, SessionFactory sessionFactory) {
        this.projectDaoApi = projectDaoApi;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Long createProject(ProjectDto projectDtoSave){
        Project projectEntity = fromDto(projectDtoSave);
        Long projectId;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                projectId = validationDto(projectEntity);
                transaction.commit();
                log.info("Save new project with id {} in {}", projectId, new Date());
            }catch (Exception exception) {
                transaction.rollback();
                log.error("Cannot commit transaction, error with db");
                throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
            }
        }
        return projectId;
    }

    private Long validationDto(Project projectEntity) throws ProjectInvalidParameterException {
        Long projectId;
        if((projectEntity.getName().length()>4 && projectEntity.getName().length()<256)
                &&(projectEntity.getDescription().length()>10&& projectEntity.getDescription().length()<512)) {
            projectId = projectDaoApi.createProject(projectEntity);
        }else {
            throw new ProjectInvalidParameterException("Project parameter is invalid, try yet");
        }
        return projectId;
    }

    @Override
    public Long updateProject(ProjectUpdateDto projectDtoSave){
        Project projectEntity = fromDto(projectDtoSave);
        Long projectId;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                projectId = projectDaoApi.updateProject(projectEntity);
                transaction.commit();
                log.info("Update project with id {} in {}",projectId,new Date());
             }catch (Exception exception) {
                transaction.rollback();
                log.error("Cannot commit transaction, error with db");
                throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
            }
        }
        return projectId;
    }

    @Override
    public Long deleteProject(Long projectId) throws ProjectNotFoundException {
        Project projectByID = projectDaoApi.getProjectById(projectId);
        if(projectByID!=null) {
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                try {
                    projectId = projectDaoApi.deleteProject(projectId);
                    transaction.commit();
                    log.info("Delete project with id {} in {}",projectId,new Date());
                }catch (Exception exception) {
                    transaction.rollback();
                    log.error("Cannot commit transaction, error with db");
                    throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
                }
            }
        }else{
            throw new ProjectNotFoundException(String.format("Project with id %s was not found",projectId));
        }
        return projectId;
    }

    @Override
    public ProjectDto getProjectById(Long projectId){
        ProjectDto projectDto;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Project projectByID = projectDaoApi.getProjectById(projectId);
                if (projectByID != null) {
                    projectDto = fromEntityWithTask(projectByID);
                    transaction.commit();
                } else {
                    throw new ProjectNotFoundException(String.format("Project with id %s was not found", projectId));
                }
            }catch (Exception exception) {
                transaction.rollback();
                log.error("Cannot commit transaction, error with db");
                throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
            }
        }
        return projectDto;
    }

    @Override
    public List<ProjectDto> getProjectByName(String name){
        List<ProjectDto> projectDtoList;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                if (!name.equals("")) {
                    transaction.commit();
                    projectDtoList = projectDaoApi.getProjectByName(name).stream().map(ProjectDtoUtil::fromEntity)
                            .collect(Collectors.toList());
                    log.info("Get project by name {} in {}",name,new Date());
                } else {
                    transaction.commit();
                    projectDtoList = projectDaoApi.getAllProject().stream().map(ProjectDtoUtil::fromEntity)
                            .collect(Collectors.toList());
                    log.info("Get all project, name equals empty string in {}",new Date());
                }
            }catch (Exception exception) {
                transaction.rollback();
                log.error("Cannot commit transaction, error with db");
                throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
            }
        }
         return projectDtoList;
    }

    @Override
    public List<ProjectDto> getAllProject(){
        List<ProjectDto> projectDtoList;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                projectDtoList = projectDaoApi.getAllProject().stream().map(ProjectDtoUtil::fromEntity)
                        .collect(Collectors.toList());
                transaction.commit();
                log.info("Get all project in {}",new Date());
            }catch (Exception exception) {
                transaction.rollback();
                log.error("Cannot commit transaction, error with db");
                throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
            }
        }
         return projectDtoList;
    }


}
