package edu.uoc.tfg.auction;

import edu.uoc.tfg.auction.application.rest.AuctionRESTController;
import edu.uoc.tfg.auction.domain.model.Auction;
import edu.uoc.tfg.auction.domain.model.AuctionStatus;
import edu.uoc.tfg.auction.domain.service.AuctionService;
import edu.uoc.tfg.auction.infrastructure.security.CustomUserDetails;
import edu.uoc.tfg.auction.infrastructure.security.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.hamcrest.Matchers.hasSize;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
class AuctionControllerUnitTest {

    @Mock
    private AuctionService auctionService;
    @Mock
    private UserService userService;
    @InjectMocks
    private AuctionRESTController auctionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(auctionController).build();
    }

    private Auction createMockAuction() {
        return Auction.builder()
                .id(1L)
                .propertyId(2L)
                .userId(3L)
                .initialPrice(new BigDecimal("100.00"))
                .status(AuctionStatus.ACTIVE)
                .endDate(LocalDateTime.now().plusDays(14))
                .build();
    }


    @Test
    @WithMockUser(username = "user", roles = {"SELLER"})
    void testCreateAuction() throws Exception {
        String jsonRequest = "{\"propertyId\": 1, \"initialPrice\": 100}";
        Auction mockAuction = createMockAuction();
        CustomUserDetails mockUserDetails = new CustomUserDetails("user", "password", new ArrayList<>(), 3L);

        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(mockUserDetails));
        when(auctionService.createAuction(any())).thenReturn(mockAuction);

        mockMvc.perform(post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Subasta creada con éxito con un precio de salida de " + mockAuction.getInitialPrice())));
    }



    @Test
    void testGetActiveAuctions() throws Exception {
        List<Auction> activeAuctions = Arrays.asList(createMockAuction(), createMockAuction());
        when(auctionService.getAllActiveAuctions()).thenReturn(activeAuctions);

        mockMvc.perform(get("/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(activeAuctions.size())));
    }

    @Test
    @WithMockUser(username = "user", roles = {"SELLER"})
    void testGetLastAuctions() throws Exception {
        List<Auction> endedAuctions = Arrays.asList(createMockAuction(), createMockAuction());
        when(auctionService.getLastAuctions()).thenReturn(endedAuctions);

        mockMvc.perform(get("/ended"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(endedAuctions.size())));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllAuctions() throws Exception {
        List<Auction> allAuctions = Arrays.asList(createMockAuction(), createMockAuction());
        when(auctionService.getAllAuctions()).thenReturn(allAuctions);

        mockMvc.perform(get("/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(allAuctions.size())));
    }
    @Test
    void testGetAuctionById() throws Exception {
        Long auctionId = 1L;
        Auction mockAuction = createMockAuction();
        when(auctionService.getAuctionById(auctionId)).thenReturn(Optional.of(mockAuction));

        mockMvc.perform(get("/" + auctionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(mockAuction.getId().intValue())));
    }

    @Test
    @WithMockUser(username = "user", roles = {"BUYER"})
    void testPlaceBid() throws Exception {
        String jsonRequest = "{\"auctionId\": 1, \"amount\": 200}";
        CustomUserDetails mockUserDetails = new CustomUserDetails("user", "password", new ArrayList<>(), 3L);

        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(mockUserDetails));
        when(auctionService.placeBid(eq(1L), eq(new BigDecimal("200")), anyLong())).thenReturn(new BigDecimal("200"));

        mockMvc.perform(post("/bid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Puja realizada correctamente por 200 EUR")));
    }



}

