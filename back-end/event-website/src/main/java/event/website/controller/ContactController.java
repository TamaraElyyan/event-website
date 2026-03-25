package event.website.controller;

import event.website.model.City;
import event.website.model.Contact;
import event.website.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/contact")
@CrossOrigin(origins = "http://localhost:5173/")

public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    // Retrieve all contacts
    @GetMapping("/contactList")
    public ResponseEntity<List<Contact>> getAllContacts() {
        List<Contact> contacts = contactService.getAllContacts();
        return ResponseEntity.ok(contacts);
    }

    // Retrieve a single contact by ID
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable("id") Integer id) {
        Contact contact = contactService.getContactById(id);
        if (contact == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contact);
    }

    // Create a new contact
    @PostMapping("/addContact")
    public ResponseEntity<?> addContact(@RequestBody @Valid Contact contact) {
        try {
            Contact newContact = contactService.saveContact(contact);
            return ResponseEntity.ok(newContact);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while saving the contact.");
        }
    }

    // Update an existing contact
    @PutMapping("/updateContact/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable("id") Integer id, @RequestBody @Valid Contact contact) {
          Contact contactobj = contactService.getContactById(id);
          if (contactobj == null) {
              throw new UsernameNotFoundException("Contact not found with ID: " + id);
          }
        contactobj.setName(contact.getName());
          contactobj.setPhone(contact.getPhone());
          contactobj.setEmail(contact.getEmail());
          contactobj.setMessage(contact.getMessage());
        Contact updatedContact = contactService.saveContact(contactobj);
        return ResponseEntity.ok(updatedContact);
    }

    @PutMapping("/addReplay/{id}")
    public ResponseEntity<Contact> addReplay(@PathVariable("id") Integer id, @RequestBody @Valid Map<String, String> requestBody) {
        // Extract the 'replay' value from the request body
        String replay = requestBody.get("replay");

        if (replay == null || replay.isEmpty()) {
            throw new IllegalArgumentException("Replay value is required.");
        }

        // Fetch the contact by ID
        Contact contact = contactService.getContactById(id);
        if (contact == null) {
            throw new UsernameNotFoundException("Contact not found with ID: " + id);
        }

        // Update the replay field
        contact.setReplay(replay);

        // Save the updated contact
        Contact updatedContact = contactService.saveContact(contact);
        return ResponseEntity.ok(updatedContact);
    }




    // Delete a contact by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteContact(@PathVariable("id") Integer id) {
        try {
            // Fetch the contact by ID to ensure it exists
            Contact contact = contactService.getContactById(id);
            if (contact == null) {
                throw new UsernameNotFoundException("Contact not found with ID: " + id);
            }

            // Proceed with the deletion
            contactService.deleteContact(id);
            return ResponseEntity.ok("Contact with ID " + id + " was deleted.");
        } catch (UsernameNotFoundException e) {
            // Contact not found, handled by custom exception handler
            throw e;
        } catch (Exception ex) {
            // Catch any other general exceptions and rethrow
            throw new RuntimeException("An error occurred while deleting the contact.", ex);
        }
    }

   //softDelete
    @DeleteMapping("/deleteContact/{id}")
    public ResponseEntity<String> deleteSoftContact(@PathVariable("id") Integer id) {
        try {
            // Fetch the contact by ID to ensure it exists
            Contact contact = contactService.getContactById(id);
            if (contact == null) {
                throw new UsernameNotFoundException("Contact not found with ID: " + id);
            }

            // Proceed with the soft delete by marking the contact as deleted
            contactService.softDeleteContact(id);

            return ResponseEntity.ok("Contact with ID " + id + " was successfully soft deleted.");
        } catch (UsernameNotFoundException e) {
            // Contact not found, handled by custom exception handler
            throw e;
        } catch (Exception ex) {
            // Catch any other general exceptions and rethrow
            throw new RuntimeException("An error occurred while soft deleting the contact.", ex);
        }
    }

}
