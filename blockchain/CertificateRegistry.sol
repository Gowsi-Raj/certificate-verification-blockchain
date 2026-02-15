// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract CertificateRegistry {

    struct Certificate {
        string hash;
        uint256 timestamp;
    }

    mapping(string => Certificate) public certificates;

    event CertificateStored(string hash, uint256 timestamp);

    function storeCertificate(string memory _hash) public {
        certificates[_hash] = Certificate(_hash, block.timestamp);
        emit CertificateStored(_hash, block.timestamp);
    }

    function verifyCertificate(string memory _hash) public view returns (bool, uint256) {
        Certificate memory cert = certificates[_hash];
        if (cert.timestamp == 0) {
            return (false, 0);
        }
        return (true, cert.timestamp);
    }
}
