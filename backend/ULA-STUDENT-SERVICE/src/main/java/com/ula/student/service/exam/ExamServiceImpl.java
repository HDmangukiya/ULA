package com.ula.student.service.exam;

import com.ula.student.api.v1.request.UpdateExamEntryRequest;
import com.ula.student.dto.ExamDTO;
import com.ula.student.dto.StudentDTO;
import com.ula.student.feign.ExamServiceFeignClient;
import com.ula.student.service.exception.EntryIsAlreadyActiveException;
import com.ula.student.service.exception.ExamNotFoundException;
import com.ula.student.service.student.StudentService;
import com.ula.student.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ExamServiceImpl implements ExamService
{

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private ExamServiceFeignClient examService;

    @Override
    public ExamDTO show(Long subjectAttendanceId, Long examId)
    {

        StudentDTO studentDTO = this.studentService.getStudent(this.userUtil.getUsername());
        if(studentDTO != null)
        {
            ExamDTO examDTO = this.examService.getExamByStudentIdAndSubjectAttendanceIdAndExamId
                    (
                            userUtil.getToken(), studentDTO.getStudentOnYear().getId(), subjectAttendanceId, examId
                    );
            return examDTO;
        } else {
            return null;
        }

    }

    @Override
    public String storeEntry(Long subjectAttendanceId, Long examId, UpdateExamEntryRequest updateExamEntryRequest)
    throws EntryIsAlreadyActiveException, ExamNotFoundException
    {

        StudentDTO studentDTO = this.studentService.getStudent(this.userUtil.getUsername());

        ExamDTO examDTO = this.show(subjectAttendanceId, examId);

        if(examDTO != null && examDTO.getExamEntry() != null)
        {
            if(examDTO.getExamEntry() != null)
            {
                if(!examDTO.getExamEntry().isActive())
                {
                    // send request to the exam service
                    return this.examService.addExamEntry
                            (
                                    userUtil.getToken(),
                                    studentDTO.getStudentOnYear().getId(),
                                    subjectAttendanceId,
                                    examId,
                                    updateExamEntryRequest
                            );
                } else {
                    throw new EntryIsAlreadyActiveException("Entry is already active");
                }
            } else {
                throw new ExamNotFoundException("Exam could not be found");
            }
        }
        return null;
    }
}
