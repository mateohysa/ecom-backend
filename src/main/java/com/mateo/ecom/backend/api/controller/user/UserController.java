package com.mateo.ecom.backend.api.controller.user;

import com.mateo.ecom.backend.models.Address;
import com.mateo.ecom.backend.models.AppUser;
import com.mateo.ecom.backend.models.dao.AddressRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final AddressRepository addressRepository;

    public UserController(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @GetMapping("/{user_id}/address")
    public ResponseEntity<List<Address>>getAddress(@AuthenticationPrincipal AppUser user,
                                                   @PathVariable Long user_id) {

        if(userHasPermission(user, user_id)){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(addressRepository.findAddressesByUser_Id(user_id));
    }

    @PutMapping("/{user_id}/address")
    public ResponseEntity<Address>putAddress(@AuthenticationPrincipal AppUser user,
                                            @PathVariable Long user_id, @RequestBody Address address) {

        if(userHasPermission(user, user_id)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        address.setId(null);
        AppUser refUser = new AppUser();
        refUser.setId(user_id);
        address.setUser(refUser);
        return ResponseEntity.ok(addressRepository.save(address));
    }
    @PatchMapping("/{user_id}/address/{addressId}")
    public ResponseEntity<Address>patchAddress(@AuthenticationPrincipal AppUser user,
                                             @PathVariable Long user_id, @PathVariable Long addressId, @RequestBody Address address) {

        if(userHasPermission(user, user_id)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (Objects.equals(addressId, address.getId())) {
            Optional<Address> opExistingAddress = addressRepository.findAddressByUser_Id(addressId);

            if (opExistingAddress.isPresent()) {
                AppUser refUser = opExistingAddress.get().getUser();
                if (Objects.equals(refUser.getId(), user_id)) {
                    address.setUser(refUser);
                    return ResponseEntity.ok(addressRepository.save(address));
                }
            }
        }
        return ResponseEntity.badRequest().build();
    }


    private boolean userHasPermission(AppUser user, Long id){
        return !Objects.equals(user.getId(), id);
    }

}
