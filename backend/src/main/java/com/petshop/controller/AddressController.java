package com.petshop.controller;

import com.petshop.common.ApiResponse;
import com.petshop.dto.AddressRequest;
import com.petshop.entity.Address;
import com.petshop.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ApiResponse<List<Address>> list() {
        return ApiResponse.success(addressService.listByUser());
    }

    @PostMapping
    public ApiResponse<Address> create(@Valid @RequestBody AddressRequest req) {
        return ApiResponse.success("地址添加成功", addressService.create(req));
    }

    @PutMapping("/{id}")
    public ApiResponse<Address> update(@PathVariable Long id, @Valid @RequestBody AddressRequest req) {
        return ApiResponse.success("地址更新成功", addressService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ApiResponse.success("地址已删除", null);
    }

    @PutMapping("/{id}/default")
    public ApiResponse<Address> setDefault(@PathVariable Long id) {
        return ApiResponse.success("已设为默认地址", addressService.setDefault(id));
    }
}
