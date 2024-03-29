package com.restful.elementary.school.management.service.impl;

import com.restful.elementary.school.management.dto.teacher.DadosCadastroTeacher;
import com.restful.elementary.school.management.dto.teacher.DadosListagemTeacher;
import com.restful.elementary.school.management.entity.Teacher;
import com.restful.elementary.school.management.exception.DuplicateEntryException;
import com.restful.elementary.school.management.exception.ResourceNotFoundException;
import com.restful.elementary.school.management.repository.TeacherRepository;
import com.restful.elementary.school.management.service.TeacherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    @Transactional
    public Teacher save(DadosCadastroTeacher dadosCadastroTeacher) {

        if(teacherRepository.existsByCpf(dadosCadastroTeacher.cpf()))
            throw new DuplicateEntryException("CPF already exists");

        if(teacherRepository.existsByEmail(dadosCadastroTeacher.email()))
            throw new DuplicateEntryException("Email already exists");

        Teacher teacher = new Teacher(dadosCadastroTeacher);
        return teacherRepository.save(teacher);
    }

    @Override
    public void delete(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        teacherRepository.delete(teacher);
    }

    @Override
    public Teacher findById(Long id) {
        return teacherRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
    }

    @Override
    public Page<DadosListagemTeacher> findAll(Pageable pageable) {
        return teacherRepository.findAll(pageable)
                .map(DadosListagemTeacher::new);
    }

    @Override
    public Page<DadosListagemTeacher> findByName(String name, Pageable pageable) {
        return teacherRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(DadosListagemTeacher::new);
    }

    @Override
    public Page<DadosListagemTeacher> findByEmail(String email, Pageable page) {
        return teacherRepository.findByEmailContainingIgnoreCase(email, page)
                .map(DadosListagemTeacher::new);
    }
}
