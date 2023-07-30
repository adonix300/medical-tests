package ru.netology.patient.service.medical;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicalServiceImplTest {
    @Test
    void testCheckBloodPressure() {
        //given
        String patientID = "id1";
        SendAlertService alertServiceMock = mock(SendAlertService.class);
        PatientInfoRepository patientInfoRepositoryMock = mock(PatientInfoRepository.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepositoryMock, alertServiceMock);

        BloodPressure currentPressure = new BloodPressure(60, 120);
        //when
        when(patientInfoRepositoryMock.getById(patientID))
                .thenReturn(new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        medicalService.checkBloodPressure(patientID, currentPressure);
        //then
        verify(alertServiceMock).send(anyString());
    }

    @Test
    void testCheckTemperature() {
        String patientID = "id1";
        SendAlertService alertServiceMock = mock(SendAlertService.class);
        PatientInfoRepository patientInfoRepositoryMock = mock(PatientInfoRepository.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepositoryMock, alertServiceMock);

        BigDecimal currentTemperature = new BigDecimal("1.5");
        //when
        when(patientInfoRepositoryMock.getById(patientID))
                .thenReturn(new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        medicalService.checkTemperature(patientID, currentTemperature);
        //then
        verify(alertServiceMock).send(anyString());
    }

    @Test
    void testWhenAllIsOk() {
        String patientID = "id1";
        SendAlertService alertServiceMock = mock(SendAlertService.class);
        PatientInfoRepository patientInfoRepositoryMock = mock(PatientInfoRepository.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepositoryMock, alertServiceMock);

        BigDecimal currentTemperature = new BigDecimal("36.6");
        BloodPressure currentPressure = new BloodPressure(120, 80);
        //when
        when(patientInfoRepositoryMock.getById(patientID))
                .thenReturn(new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        medicalService.checkTemperature(patientID, currentTemperature);
        medicalService.checkBloodPressure(patientID, currentPressure);
        //then
        verify(alertServiceMock, never()).send(anyString());
    }

}