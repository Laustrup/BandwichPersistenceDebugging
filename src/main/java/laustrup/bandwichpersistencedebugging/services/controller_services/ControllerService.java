package laustrup.bandwichpersistencedebugging.services.controller_services;

import laustrup.bandwichpersistencedebugging.models.Response;
import laustrup.bandwichpersistencedebugging.models.Search;
import laustrup.bandwichpersistencedebugging.utilities.Liszt;
import laustrup.bandwichpersistencedebugging.utilities.Plato;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ControllerService<E> {

    /**
     * Will create a ResponseEntity with status of whether the content is null or not.
     * @param element The E element that is either null or not and should be returned.
     * @return The created ResponseEntity of an E element.
     */
    protected ResponseEntity<Response<E>> entityContent(E element) {
        if (element != null)
            return new ResponseEntity<>(new Response<>(element), HttpStatus.OK);
        else
            return new ResponseEntity<>(new Response<>(null, Response.StatusType.NO_CONTENT),
                    HttpStatus.NO_CONTENT);
    }

    /**
     * Will create a ResponseEntity with status of whether the content is null or not.
     * @param elements The E elements that is either null or not and should be returned.
     * @return The created ResponseEntity of E elements.
     */
    protected ResponseEntity<Response<E[]>> entityContent(Liszt<E> elements) {
        if (elements != null)
            return new ResponseEntity<>(new Response<>((E[]) elements.toArray()), HttpStatus.OK);
        else
            return new ResponseEntity<>(new Response<>(null, Response.StatusType.NO_CONTENT),
                HttpStatus.NO_CONTENT);
    }

    /**
     * Will create a ResponseEntity with status of whether the content is null or not.
     * @param elements The E elements that is either null or not and should be returned.
     * @return The created ResponseEntity of E elements.
     */
    protected ResponseEntity<Response<E[]>> entityContent(E[] elements) {
        if (elements != null)
            return new ResponseEntity<>(new Response<>(elements), HttpStatus.OK);
        else
            return new ResponseEntity<>(new Response<>(null, Response.StatusType.NO_CONTENT),
                    HttpStatus.NO_CONTENT);
    }

    /**
     * Will create a ResponseEntity with status of whether the content is null or not.
     * @param search The Search that is either null or not and should be returned.
     * @return The created ResponseEntity of a Search.
     */
    protected ResponseEntity<Response<Search>> searchContent(Search search) {
        if (search != null)
            return new ResponseEntity<>(new Response<>(search), HttpStatus.OK);
        else
            return new ResponseEntity<>(new Response<>(null, Response.StatusType.NO_CONTENT),
                HttpStatus.NO_CONTENT);
    }

    protected ResponseEntity<Response<Plato.Argument>> platoContent(Plato plato) {
        if (plato.get_message()!=null) {
            if (plato.get_message().isEmpty())
                return new ResponseEntity<>(new Response<>(plato.get_argument(), Response.StatusType.UNKNOWN),
                        HttpStatus.NOT_ACCEPTABLE);
            else
                return new ResponseEntity<>(new Response<>(plato.get_argument(), Response.StatusType.NOT_ACCEPTABLE),
                        HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(new Response<>(plato.get_argument()),HttpStatus.OK);
    }
}
