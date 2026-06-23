package com.petshop.service;

import com.petshop.common.BusinessException;
import com.petshop.dto.AddressRequest;
import com.petshop.entity.Address;
import com.petshop.repository.AddressRepository;
import com.petshop.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public List<Address> listByUser() {
        return addressRepository.findByUserIdOrderByIsDefaultDesc(CurrentUser.getUserId());
    }

    @Transactional
    public Address create(AddressRequest req) {
        // 若设为默认，取消其他默认
        if (req.getIsDefault() != null && req.getIsDefault() == 1) {
            clearDefault();
        }
        Address address = new Address();
        address.setUserId(CurrentUser.getUserId());
        address.setName(req.getName());
        address.setPhone(req.getPhone());
        address.setAddress(req.getAddress());
        address.setIsDefault(req.getIsDefault() != null ? req.getIsDefault() : 0);
        return addressRepository.save(address);
    }

    @Transactional
    public Address update(Long id, AddressRequest req) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new BusinessException("地址不存在"));
        if (!address.getUserId().equals(CurrentUser.getUserId())) {
            throw new BusinessException(403, "无权操作");
        }
        if (req.getIsDefault() != null && req.getIsDefault() == 1) {
            clearDefault();
        }
        if (req.getName() != null) address.setName(req.getName());
        if (req.getPhone() != null) address.setPhone(req.getPhone());
        if (req.getAddress() != null) address.setAddress(req.getAddress());
        if (req.getIsDefault() != null) address.setIsDefault(req.getIsDefault());
        return addressRepository.save(address);
    }

    @Transactional
    public void delete(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new BusinessException("地址不存在"));
        if (!address.getUserId().equals(CurrentUser.getUserId())) {
            throw new BusinessException(403, "无权操作");
        }
        addressRepository.delete(address);
    }

    @Transactional
    public Address setDefault(Long id) {
        clearDefault();
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new BusinessException("地址不存在"));
        if (!address.getUserId().equals(CurrentUser.getUserId())) {
            throw new BusinessException(403, "无权操作");
        }
        address.setIsDefault(1);
        return addressRepository.save(address);
    }

    private void clearDefault() {
        List<Address> defaults = addressRepository.findByUserIdAndIsDefault(CurrentUser.getUserId(), 1);
        for (Address a : defaults) {
            a.setIsDefault(0);
            addressRepository.save(a);
        }
    }
}
