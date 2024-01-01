package edu.uod.tfg.property;

import edu.uod.tfg.property.application.rest.PropertyRESTController;
import edu.uod.tfg.property.domain.model.OwnerHistory;
import edu.uod.tfg.property.domain.model.Property;
import edu.uod.tfg.property.domain.service.AddressService;
import edu.uod.tfg.property.domain.service.PropertyService;
import edu.uod.tfg.property.infrastructure.security.CustomUserDetails;
import edu.uod.tfg.property.infrastructure.security.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.hamcrest.Matchers.hasSize;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PropertyControllerUnitTest {

    private MockMvc mockMvc;

    @Mock
    private PropertyService propertyService;
    @Mock
    private AddressService addressService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PropertyRESTController propertyRESTController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(propertyRESTController).build();

    }

    @Test
    public void testFindProperties() throws Exception {
        List<Property> properties = Arrays.asList(new Property(), new Property());
        when(propertyService.findAllProperties()).thenReturn(properties);

        mockMvc.perform(get("/properties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetUserProperties() throws Exception {
        Long userId = 4L;
        List<Property> properties = Arrays.asList(new Property(), new Property());
        CustomUserDetails mockUserDetails = new CustomUserDetails("admin", "password", new ArrayList<>(), 4L);

        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(mockUserDetails));
        when(propertyService.findPropertiesByUserExcludingDeleted(userId)).thenReturn(properties);

        mockMvc.perform(get("/properties/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetPropertyById() throws Exception {
        Long propertyId = 1L;
        when(propertyService.findPropertyById(propertyId)).thenReturn(Optional.of(new Property()));

        mockMvc.perform(get("/property/" + propertyId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testFindOwners() throws Exception {
        List<OwnerHistory> owners = Arrays.asList(new OwnerHistory(), new OwnerHistory());
        when(propertyService.findAllOwnersHistory()).thenReturn(owners);

        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetPropertiesInAuction() throws Exception {
        List<Property> properties = Arrays.asList(new Property(), new Property());
        when(propertyService.findAllPropertiesInAuction()).thenReturn(properties);

        mockMvc.perform(get("/auction"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetPropertiesPendingValidation() throws Exception {
        List<Property> properties = Arrays.asList(new Property(), new Property());
        when(propertyService.findAllPendingValidation()).thenReturn(properties);

        mockMvc.perform(get("/pending-validation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testValidateProperty() throws Exception {
        Long propertyId = 1L;
        when(propertyService.validateProperty(propertyId)).thenReturn(new Property());

        mockMvc.perform(put("/validate/" + propertyId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testInvalidateProperty() throws Exception {
        Long propertyId = 1L;
        when(propertyService.invalidateProperty(propertyId)).thenReturn(new Property());

        mockMvc.perform(put("/invalidate/" + propertyId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testDeleteProperty() throws Exception {
        Long propertyId = 1L;
        CustomUserDetails mockUserDetails = new CustomUserDetails("user", "password", new ArrayList<>(), 1L);

        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(mockUserDetails));
        when(propertyService.deleteProperty(propertyId, 1L)).thenReturn(new Property());

        mockMvc.perform(put("/delete/" + propertyId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"SELLER"})
    public void testCreateProperty() throws Exception {
        String newPropertyJson = "{\"type\":\"VIVIENDA\",\"description\":\"Piso en Barcelona\",\"rooms\":3,\"baths\":2,\"surface\":120.5,\"registryDocumentUrl\":\"https://example.com/registry.pdf\",\"catastralReference\":\"12345BCN\",\"latitude\":41.38879,\"longitude\":2.15899,\"address\":\"Calle Falsa 123, Barcelona\",\"postalCode\":\"08001\",\"city\":\"Barcelona\",\"region\":\"Cataluña\",\"country\":\"España\"}";

        CustomUserDetails mockUserDetails = new CustomUserDetails("user", "password", new ArrayList<>(), 4L);

        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(mockUserDetails));

        when(propertyService.createProperty(any())).thenReturn(new Property());

        mockMvc.perform(post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPropertyJson))
                .andExpect(status().isOk());
    }
}

