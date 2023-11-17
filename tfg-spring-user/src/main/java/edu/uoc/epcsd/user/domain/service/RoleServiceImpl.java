package edu.uoc.epcsd.user.domain.service;


import edu.uoc.epcsd.user.domain.Role;
import edu.uoc.epcsd.user.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class RoleServiceImpl implements RoleService{
    private final RoleRepository roleRepository;
    @Override
    public Role getSellerRole(){
        return roleRepository.getSellerRole()
                .orElseThrow(() -> new RuntimeException("Rol por defecto no encontrado"));
    }
    @Override
    public Role getDefaultRole(){
        return roleRepository.getDefaultRole()
                .orElseThrow(() -> new RuntimeException("Rol por defecto no encontrado"));
    }
}
