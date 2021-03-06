package com.mediscreen_patient.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen_patient.model.Patient;
import com.mediscreen_patient.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientRestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PatientService patientServiceMock;


  @Test
  public void testGetPatients() throws Exception {
    mockMvc.perform(get("/patients")).andExpect(status().isOk());
  }

  @Test
  public void testDoesPatientExist() throws Exception {
    int id = 1;

    when(patientServiceMock.patientExist(id)).thenReturn(true);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/patientExists?id=1"))
        .andExpect(status().isOk());
  }

  @Test
  public void testGetPatient() throws Exception {
    int id = 1;
    Patient patient = new Patient();
    patient.setId(1);
    patient.setGiven("adrien");
    patient.setFamily("Gaiveron");
    patient.setDob("2004-06-18");
    patient.setSex("M");

    when(patientServiceMock.patientExist(id)).thenReturn(true);
    when(patientServiceMock.getPatient(id)).thenReturn(patient);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/patient?id=1"))
        .andExpect(status().isOk());
  }

  @Test
  public void testGetPatientByFamilyName() throws Exception {
    Patient patient = new Patient();
    patient.setGiven("adrien");
    patient.setFamily("Bessy");
    patient.setDob("2004-06-18");
    patient.setSex("M");

    when(patientServiceMock.getPatient("Bessy")).thenReturn(patient);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/patientByFamilyName?familyName=Bessy"))
        .andExpect(status().isOk());
  }


  @Test
  public void testCreatePatient() throws Exception {
    Patient patient = new Patient();
    patient.setGiven("adrien");
    patient.setFamily("Gaiveron");
    patient.setDob("2004-06-18");
    patient.setSex("M");

    when(patientServiceMock.savePatient(patient)).thenReturn(patient);

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/patient")
        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
        .content(new ObjectMapper().writeValueAsString(patient));
    this.mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk());
  }


  @Test
  public void testUpdatePatient() throws Exception {
    Patient patient = new Patient();
    patient.setId(1);
    patient.setGiven("adrien");
    patient.setFamily("Gaiveron");
    patient.setDob("2004-06-18");
    patient.setSex("M");

    when(patientServiceMock.patientExist(patient.getId())).thenReturn(true);
    doNothing().when(patientServiceMock).updatePatient(patient.getId(), patient);

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/patient/" + patient.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
        .content(new ObjectMapper().writeValueAsString(patient));
    this.mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk());
  }


  @Test
  public void testDeletePatient() throws Exception {
    int id = 1;
    Patient patient = new Patient();
    patient.setId(1);
    patient.setGiven("adrien");
    patient.setFamily("Gaiveron");
    patient.setDob("2004-06-18");
    patient.setSex("M");

    when(patientServiceMock.patientExist(id)).thenReturn(true);
    when(patientServiceMock.deletePatient(id)).thenReturn(patient);

    mockMvc
        .perform(MockMvcRequestBuilders.delete("/patient?id=1"))
        .andExpect(status().isOk());
  }


}
