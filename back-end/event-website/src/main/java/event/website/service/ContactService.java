package event.website.service;

import event.website.model.Contact;
import event.website.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public Contact getContactById(Integer id) {
        Optional<Contact> contact = contactRepository.findById(id);
        return contact.orElse(null); // Return the contact if found, otherwise return null
    }

    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }


    public void softDeleteContact(Integer id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Contact not found with ID: " + id));

        // Mark the contact as deleted by setting the deleted flag to true
        contact.setDeleted(true);

        // Save the updated contact record
        contactRepository.save(contact);
    }

    public void deleteContact(Integer id) {
        contactRepository.deleteById(id);
    }
}
