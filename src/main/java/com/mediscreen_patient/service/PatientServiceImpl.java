package com.mediscreen_patient.service;

import com.mediscreen_patient.exceptions.IsForbiddenException;
import com.mediscreen_patient.exceptions.NonexistentException;
import com.mediscreen_patient.model.Patient;
import com.mediscreen_patient.repositories.PatientRepository;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl implements PatientService {

  private static final Logger logger = LogManager.getLogger(PatientServiceImpl.class);

  @Autowired
  private PatientRepository patientRepository;

  /**
   * Get all patients
   * 
   * @return all patients
   */
  @Override
  public List<Patient> getPatients() {
    logger.debug("in the method getPatients in the class PatientServiceImpl");
    List<Patient> patientList = new ArrayList<>();
    patientList = patientRepository.findAll();
    return patientList;
  }

  /**
   * Save a patient
   * 
   * @param patient A patient to save
   * @return the saved patient
   */
  @Override
  public Patient savePatient(Patient patient) {
    logger.debug("in the method savePatient in the class PatientServiceImpl");
    if (patientRepository
        .existsByGivenAndFamilyAndDobAllIgnoreCase(patient.getGiven(), patient.getFamily(),
            patient.getDob())) {
      throw new IsForbiddenException("This patient already exists.");
    }
    Patient savedPatient = patientRepository.save(patient);
    return savedPatient;
  }

  /**
   * Check if the given patient id exists.
   * 
   * @param id The patient id
   * @return true if the id exists, otherwise returns false
   */
  @Override
  public boolean patientExist(Integer id) {
    logger.debug("in the method patientExist in the class PatientServiceImpl");
    boolean patientExist = false;
    patientExist = patientRepository.existsById(id);
    if (!patientExist) {
      logger.error("The patient with the id " + id + " doesn't exist.");
      throw new NonexistentException(
          "The patient with the id " + id + " doesn't exist.");
    }
    return patientExist;
  }

  /**
   * Get a patient from an id
   * 
   * @param id The id of the patient table
   * @return The patientname
   */
  @Override
  public Patient getPatient(Integer id) {
    logger.debug("in the method getPatient in the class PatientServiceImpl");
    Patient patient = null;
    patient = patientRepository.findById(id);
    return patient;
  }

  /**
   * Update a patient
   * 
   * @param id      The id of the patient to update
   * @param patient A patient to update
   */
  @Override
  public void updatePatient(Integer id, Patient patient) {
    logger.debug("in the method updatePatient in the class PatientServiceImpl");
    Patient patientToUpdate = patientRepository.findById(id);
    if (patientRepository
        .existsByGivenAndFamilyAndDobAllIgnoreCase(patient.getGiven(), patient.getFamily(),
            patient.getDob())
        &&
        (!patient.getGiven().equals(patientToUpdate.getGiven())
            || !patient.getFamily().equals(patientToUpdate.getFamily())
            || !patient.getDob().equals(patientToUpdate.getDob()))) {
      throw new IsForbiddenException("This patient already exists.");
    }
    if (patient.getGiven() != null) {
      patientToUpdate.setGiven(patient.getGiven());
    }
    if (patient.getFamily() != null) {
      patientToUpdate.setFamily(patient.getFamily());
    }
    if (patient.getDob() != null) {
      patientToUpdate.setDob(patient.getDob());
    }
    if (patient.getSex() != null) {
      patientToUpdate.setSex(patient.getSex());
    }
    if (patient.getAddress() != null) {
      patientToUpdate.setAddress(patient.getAddress());
    }
    if (patient.getPhone() != null) {
      patientToUpdate.setPhone(patient.getPhone());
    }
    patientRepository.save(patientToUpdate);
  }

  /**
   * Delete a patient
   * 
   * @param id A patient id
   * @return the deleted patient
   */
  @Override
  @Transactional
  public Patient deletePatient(Integer id) {
    logger.debug("in the method deletePatient in the class PatientServiceImpl");
    Patient patient = patientRepository.findById(id);
    patientRepository.deleteById(id);
    return patient;
  }

  @Override
  public Patient getPatient(String familyName) {
    int familyNameOccurences = patientRepository.countByFamily(familyName);
    if (familyNameOccurences == 0) {
      logger.error("The patient with the family name " + familyName + " doesn't exist.");
      throw new NonexistentException(
          "The patient with the family name " + familyName + " doesn't exist.");
    }
    if (familyNameOccurences > 1) {
      logger.error(
          "There are several patients with this family name, try the request with his patient id: curl http://.../assess?patientId={id}");
      throw new IsForbiddenException(
          "There are several patients with this family name, try the request with his patient id: curl http://.../assess?patientId={id}");
    }
    Patient patient = patientRepository.findByFamily(familyName);
    return patient;
  }

}
