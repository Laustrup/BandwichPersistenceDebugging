package laustrup.bandwichpersistencedebugging.services.controller_services.sub_controller_services;

import laustrup.bandwichpersistencedebugging.models.Response;
import laustrup.bandwichpersistencedebugging.models.chats.ChatRoom;
import laustrup.bandwichpersistencedebugging.models.chats.messages.Mail;
import laustrup.bandwichpersistencedebugging.models.dtos.chats.ChatRoomDTO;
import laustrup.bandwichpersistencedebugging.services.controller_services.ControllerService;
import laustrup.bandwichpersistencedebugging.services.persistence_services.entity_services.sub_entity_services.UserPersistenceService;

import org.springframework.http.ResponseEntity;

public class ChatRoomControllerService extends ControllerService<ChatRoomDTO> {

    /**
     * Singleton instance of the Service.
     */
    private static ChatRoomControllerService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static ChatRoomControllerService get_instance() {
        if (_instance == null) _instance = new ChatRoomControllerService();
        return _instance;
    }

    private ChatRoomControllerService() {}

    /**
     * Upserts a Mail, will insert depending on if there is an id
     * or if the id already exists, in that case it will update it.
     * @param mail The Mail that will be upserted.
     * @return A ResponseEntity with the Response of the ChatRoom of the Mail and the HttpStatus.
     */
    public ResponseEntity<Response<ChatRoomDTO>> upsert(Mail mail) {
        return entityContent(new ChatRoomDTO(UserPersistenceService.get_instance().upsert(mail)));
    }

    /**
     * Upserts a ChatRoom, will insert depending on if there is an id
     * or if the id already exists, in that case it will update it.
     * Also inserts Chatters if they exist.
     * @param chatRoom The ChatRoom that will be upserted.
     * @return A ResponseEntity with the Response of the ChatRoom and the HttpStatus.
     */
    public ResponseEntity<Response<ChatRoomDTO>> upsert(ChatRoom chatRoom) {
        return entityContent(new ChatRoomDTO(UserPersistenceService.get_instance().upsert(chatRoom)));
    }
}
