package laustrup.bandwichpersistencedebugging.models.dtos.chats.messages;

import laustrup.bandwichpersistencedebugging.models.chats.messages.Mail;
import laustrup.bandwichpersistencedebugging.models.dtos.chats.ChatRoomDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @Data
public class MailDTO extends MessageDTO {

    private ChatRoomDTO _chatRoom;

    public MailDTO(Mail mail) {
        super(mail.get_primaryId(), mail.get_author(), mail.get_content(),
                mail.is_sent(), mail.get_edited(), mail.is_public(), mail.get_timestamp());
        _chatRoom = new ChatRoomDTO(mail.get_chatRoom());
    }
}
