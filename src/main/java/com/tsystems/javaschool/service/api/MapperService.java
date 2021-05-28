package com.tsystems.javaschool.service.api;

import com.tsystems.javaschool.model.dto.*;
import com.tsystems.javaschool.model.entity.*;
import org.springframework.stereotype.Service;

@Service
public interface MapperService {
        PatientDto convertToDto(Patient patientEntity);
        Patient convertToEntity(PatientDto dto);

        TreatmentDto convertToDto(Treatment treatment);
        Treatment convertToEntity(TreatmentDto dto);

        TherapyDto convertToDto(Therapy therapy);
        Therapy convertToEntity(TherapyDto dto);

        TherapyCase convertToEntity(TherapyCaseDto dto);
        TherapyCaseDto converToDto(TherapyCase therapyCase);

        InsuranceDto convertToDto(Insurance insurance);

        UserDto convertToDto(UserEntity userEntity);
}
